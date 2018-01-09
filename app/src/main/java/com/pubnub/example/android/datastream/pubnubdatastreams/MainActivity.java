package com.pubnub.example.android.datastream.pubnubdatastreams;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.common.collect.ImmutableMap;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.example.android.datastream.pubnubdatastreams.multi.MultiListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.multi.MultiPnCallback;
import com.pubnub.example.android.datastream.pubnubdatastreams.presence.PresenceListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.presence.PresencePnCallback;
import com.pubnub.example.android.datastream.pubnubdatastreams.presence.PresencePojo;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.AdapterPerson;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.Person;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PostVariables;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubListAdapter;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubPnCallback;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubPojo;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.PubSubTabContentFragment;
import com.pubnub.example.android.datastream.pubnubdatastreams.pubsub.Samples;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.DateTimeUtil;
import com.pubnub.example.android.datastream.pubnubdatastreams.util.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final List<String> MULTI_CHANNELS = Arrays.asList(Constants.MULTI_CHANNEL_NAMES.split(","));
    public static final List<String> PUBSUB_CHANNEL = Arrays.asList(Constants.CHANNEL_NAME.split(","));
    private ArrayList<Person> myListItems = new ArrayList<Person>();
    private ScheduledExecutorService mScheduleTaskExecutor;
    private List<String> theChannel = new ArrayList<>();
    private List<String> subbscribechannel = new ArrayList<>();

    private PubNub mPubnub_DataStream;
    private PubSubListAdapter mPubSub;
    private PubSubPnCallback mPubSubPnCallback;

    private PresenceListAdapter mPresence;
    private PresencePnCallback mPresencePnCallback;

    private PubNub mPubnub_Multi;
    private MultiListAdapter mMulti;
    private MultiPnCallback mMultiPnCallback;

    private PNCallback<PNHistoryResult> hitoryResult;
    public static String mUsername;
    private Random random = new Random();
    private String channel;
    public static SharedPreferences mSharedPrefs;
    private ListView listView;


    private boolean isSearchOpened = false;
    private MaterialSearchView searchView;
    private AdapterPerson adbPerson;
    private LinearLayout linearLayout;
    private PubSubPojo msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mSharedPrefs == null) {
            mSharedPrefs = getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE);
            if (!mSharedPrefs.contains(Constants.DATASTREAM_UUID)) {
                Intent toLogin = new Intent(this, LoginActivity.class);
                startActivity(toLogin);
                return;
            }
        }
        mUsername = mSharedPrefs.getString(Constants.DATASTREAM_UUID, "");
        this.mPubSub = new PubSubListAdapter(this);
        this.mPresence = new PresenceListAdapter(this);
        this.mMulti = new MultiListAdapter(this);

        this.mPubSubPnCallback = new PubSubPnCallback(this.mPubSub);
        this.mPresencePnCallback = new PresencePnCallback(this.mPresence);
        this.mMultiPnCallback = new MultiPnCallback(this.mMulti);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        linearLayout = (LinearLayout) findViewById(R.id.toolbarLinearLayout);
        initSearchView();
//        initChanelList();
        theChannel.clear();
        theChannel.add(PUBSUB_CHANNEL.get(0));
//        myListItems.add(new Person("Ardit", "http://i.imgur.com/DvpvklR.png",theChannel.get(0)));
        myListItems = new Samples().getData(mUsername);
        initPubNub();


        for (Person person : new Samples().getData(mUsername)) {
            subbscribechannel.add(person.channel);
        }
        initChannels();
        listView = findViewById(R.id.chatDialogs);
        fillListView();

    }


    private void history(String channel, final int index) {
        mPubnub_DataStream.history().channel(channel).count(1).async(new PNCallback<PNHistoryResult>() {
            @Override
            public void onResponse(PNHistoryResult result, PNStatus status) {
                if (!status.isError()) {


                    PNHistoryItemResult itemResult = result.getMessages().get(0);
                    try {
                        msg = JsonUtil.convert(itemResult.getEntry(), PubSubPojo.class);
                        Person person = myListItems.get(index);
                        person.lastMessage = msg.getMessageFromType();
                        adbPerson.update(person, index);
//                        adbPerson.add(person);
//                        myListItems.get(index).lastMessage=msg.getMessageFromType();
                        Log.v("History of Channel", itemResult.getEntry().toString());
                        Log.v("History of Channel", itemResult.getEntry().get("message").toString());
                        Log.v("History of Channel", itemResult.getEntry().get("message").asText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void initSearchView() {

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                linearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onSearchViewClosed() {
                //If closed Search View , lstView will return default
                linearLayout.setVisibility(View.VISIBLE);
                fillListView();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()) {
                    ArrayList<Person> lstFound = new ArrayList<>();
                    for (Person item : myListItems) {
                        if (item.name.toLowerCase().contains(newText.toLowerCase()))
                            lstFound.add(item);
                    }
                    fillListView(lstFound);
                } else {
                    //if search text is null
                    //return default
                    fillListView();
                }
                return true;
            }

        });
    }

    private void fillListView() {
        fillListView(null);
    }

    private void fillListView(ArrayList<Person> lstFound) {
        listView = findViewById(R.id.chatDialogs);

        if (lstFound == null) {
            lstFound = myListItems;
        }

        adbPerson = new AdapterPerson(MainActivity.this, 0,lstFound);

//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_item, theChannel);
        listView.setAdapter(adbPerson);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startChat(position);
            }
        });
        for (int i = 0; i < adbPerson.getlPerson().size(); i++) {
            history(adbPerson.getlPerson().get(i).channel, i);
        }
    }

    private void startChat(int postition) {
//        try {
        List<String> chnannel = new ArrayList<>();
        Intent intent = new Intent(this, PubSubTabContentFragment.class);
        PostVariables.mPubSub = this.mPubSub;
        PostVariables.mUsername = this.mUsername;
        PostVariables.mPubnub_DataStream = this.mPubnub_DataStream;
//            PostVariables.channel = this.theChannel.get(postition);
        PostVariables.person = this.myListItems.get(postition);
        chnannel.add(PostVariables.person.channel);

        startActivity(intent);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    public void openChat(View view) {
        Intent intent = new Intent(this, PubSubTabContentFragment.class);
        PostVariables.mPubSub = this.mPubSub;
        PostVariables.mUsername = this.mUsername;
        PostVariables.mPubnub_DataStream = this.mPubnub_DataStream;
        PostVariables.channel = this.theChannel.get(0);
        startActivity(intent);
    }

    private void initChanelList() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select One Name:-");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
        for (String channel : PUBSUB_CHANNEL) {
            arrayAdapter.add(channel);
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);
                theChannel.clear();
                theChannel.add(strName);
//                AlertDialog.Builder builderInner = new AlertDialog.Builder(MainActivity.this);
//                builderInner.setMessage(strName);
//                builderInner.setTitle("Your Selected Item is");
//                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builderInner.show();
                initChannels();
                mUsername = mUsername + strName;
            }
        });
        builderSingle.show();
    }

    public void publish(View view) {
        final EditText mMessage = (EditText) MainActivity.this.findViewById(R.id.new_message);

        final Map<String, String> message = ImmutableMap.<String, String>of("sender", MainActivity.this.mUsername, "message", mMessage.getText().toString(), "timestamp", DateTimeUtil.getTimeStampUtc());

//        MainActivity.this.mPubnub_DataStream.publish().channel(Constants.CHANNEL_NAME).message(message).async(
        MainActivity.this.mPubnub_DataStream.publish().channel(theChannel.get(0)).message(message).async(

                new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        try {
                            if (!status.isError()) {
                                mMessage.setText("");
                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                            } else {
                                Log.v(TAG, "publishErr(" + JsonUtil.asJson(status) + ")");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

    }

    private final void initPubNub() {
        PNConfiguration config = new PNConfiguration();

        config.setPublishKey(Constants.PUBNUB_PUBLISH_KEY);
        config.setSubscribeKey(Constants.PUBNUB_SUBSCRIBE_KEY);
        config.setUuid(this.mUsername);
        config.setSecure(true);

        this.mPubnub_DataStream = new PubNub(config);
        this.mPubnub_Multi = new PubNub(config);
    }

    private final void initChannels() {
        this.mPubnub_DataStream.addListener(this.mPubSubPnCallback);
        this.mPubnub_DataStream.addListener(this.mPresencePnCallback);

        this.mPubnub_DataStream.subscribe().channels(subbscribechannel).withPresence().execute();
        this.mPubnub_DataStream.hereNow().channels(subbscribechannel).async(new PNCallback<PNHereNowResult>() {
            @Override
            public void onResponse(PNHereNowResult result, PNStatus status) {
                if (status.isError()) {
                    return;
                }
                try {
                    Log.v(TAG, JsonUtil.asJson(result));

                    for (Map.Entry<String, PNHereNowChannelData> entry : result.getChannels().entrySet()) {
                        for (PNHereNowOccupantData occupant : entry.getValue().getOccupants()) {
                            MainActivity.this.mPresence.add(new PresencePojo(occupant.getUuid(), "join", DateTimeUtil.getTimeStampUtc()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        this.mPubnub_Multi.addListener(mMultiPnCallback);
        this.mPubnub_Multi.subscribe().channels(MULTI_CHANNELS).execute();


        this.mScheduleTaskExecutor = Executors.newScheduledThreadPool(1);
        this.mScheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int randomIndex = random.nextInt(MULTI_CHANNELS.size());
                        String toChannel = MULTI_CHANNELS.get(randomIndex);

                        final Map<String, String> message = ImmutableMap.<String, String>of("sender", MainActivity.this.mUsername, "message", "randomly fired on this channel", "timestamp", DateTimeUtil.getTimeStampUtc());

                        MainActivity.this.mPubnub_Multi.publish().channel(toChannel).message(message).async(
                                new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        try {
                                            if (!status.isError()) {
                                                Log.v(TAG, "publish(" + JsonUtil.asJson(result) + ")");
                                            } else {
                                                Log.v(TAG, "publishErr(" + JsonUtil.asJson(status) + ")");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                        );
                    }
                });

            }
        }, 0, 15, TimeUnit.SECONDS);
    }


    private void disconnectAndCleanup() {
        getSharedPreferences(Constants.DATASTREAM_PREFS, MODE_PRIVATE).edit().clear().commit();

        if (this.mPubnub_DataStream != null) {
            this.mPubnub_DataStream.unsubscribe().channels(PUBSUB_CHANNEL).execute();
            this.mPubnub_DataStream.removeListener(this.mPubSubPnCallback);
            this.mPubnub_DataStream.removeListener(this.mPresencePnCallback);
            this.mPubnub_DataStream.stop();
            this.mPubnub_DataStream = null;
        }

        if (this.mPubnub_Multi != null) {
            this.mPubnub_Multi.unsubscribe().channels(MULTI_CHANNELS).execute();
            this.mPubnub_Multi.removeListener(this.mMultiPnCallback);
            this.mPubnub_Multi.stop();
            this.mPubnub_Multi = null;
        }

        if (this.mScheduleTaskExecutor != null) {
            this.mScheduleTaskExecutor.shutdownNow();
            this.mScheduleTaskExecutor = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout(View v) {
        logout();
    }

    public void logout() {
        disconnectAndCleanup();

        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnectAndCleanup();
    }

    public void conversaton(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
