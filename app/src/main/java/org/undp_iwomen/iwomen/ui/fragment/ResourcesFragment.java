package org.undp_iwomen.iwomen.ui.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smk.clientapi.NetworkEngine;
import com.smk.iwomen.BaseActionBarActivity;
import com.smk.model.Rating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.undp_iwomen.iwomen.R;
import org.undp_iwomen.iwomen.data.ResourceItem;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.model.retrofit_api.ResourceAPI;
import org.undp_iwomen.iwomen.provider.IwomenProviderData;
import org.undp_iwomen.iwomen.ui.activity.SubResourceListActivity;
import org.undp_iwomen.iwomen.ui.adapter.ResourcesListViewAdapter;
import org.undp_iwomen.iwomen.utils.Connection;
import org.undp_iwomen.iwomen.utils.StorageUtil;
import org.undp_iwomen.iwomen.utils.Utils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by khinsandar on 7/29/15.
 */
public class ResourcesFragment extends Fragment  {
    public static final String ARG_MENU_INDEX = "index";

    private Context mContext;
    private ListView lvResouces;
    private ArrayList<ResourceItem> ResourceItems;

    private ResourcesListViewAdapter adapter;
    SharedPreferences sharePrefLanguageUtil;
    String mstr_lang;
    String name[];
    public Rating avgRatings;

    int[] randomImgMain = new int[]{R.drawable.idea
            , R.drawable.donors, R.drawable.law};

    private ProgressDialog mProgressDialog;

    private StorageUtil storageUtil;


    private int offsetlimit = 3;
    private int skipLimit = 0;
    private Menu menu;


    public ResourcesFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //storageUtil = StorageUtil.getInstance(getActivity().getApplicationContext());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resources, container, false);
        mContext = getActivity().getApplicationContext();
        storageUtil = StorageUtil.getInstance(mContext);

        //LayoutInflater footerinflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //footer = (View) inflater.inflate(R.layout.loading_layout, null);

        init(rootView);

        getReview();
        return rootView;
    }

    private void init(View rootView) {
        sharePrefLanguageUtil = getActivity().getSharedPreferences(Utils.PREF_SETTING, Context.MODE_PRIVATE);

        ResourceItems = new ArrayList<ResourceItem>();
        lvResouces = (ListView) rootView.findViewById(R.id.resource_list);


        mstr_lang = sharePrefLanguageUtil.getString(com.parse.utils.Utils.PREF_SETTING_LANG, com.parse.utils.Utils.ENG_LANG);




        //When very start this fragment open , need to check db data
        String selections = TableAndColumnsName.ResourceUtil.STATUS + "=?";
        String[] selectionargs = {"0"};
        Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC");//DESC


        if (cursor.getCount() > 0) {
            Log.e("cursor.getCount()", "===>" + cursor.getCount());
            setupAdapter();


        } else {

            if (Connection.isOnline(getActivity())) {
                //getPostDataOrderByLikesDate();
                getResourceDataFromSever();
            } else {

                mProgressDialog.dismiss();


                if (mstr_lang.equals(Utils.ENG_LANG)) {
                    Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
                } else {

                    Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
                }
            }
        }


        lvResouces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(mContext, SubResourceListActivity.class);

                intent.putExtra("ResourceId", ResourceItems.get(i).getResourceId());
                intent.putExtra("TitleEng", ResourceItems.get(i).getResourceName());
                intent.putExtra("TitleMM", ResourceItems.get(i).getResourceNameMM());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });


    }


    private void setupAdapter() {

        if (getActivity().getApplicationContext() != null) {


            //TODO ADMIN ACCOUNT POST FILTER
            String selections = TableAndColumnsName.ResourceUtil.STATUS + "=?";
            String[] selectionargs = {"0"};
            Cursor cursor = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, selections, selectionargs, BaseColumns._ID + " DESC");//DESC

            //Log.e("Set Up adapter cursor.getCount()", "===>" + cursor.getCount());
            String id;
            String resouceEng;
            String resouceMM;
            String iconPath;
            ResourceItems.clear();
            try {

                if (cursor != null && cursor.moveToFirst()) {
                    int i = 0;
                    do {
                        //Log.e("Set Up adapter cursor.moveToFirst()", "===>" + cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID)));

                        id = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID));

                        resouceEng = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG));
                        resouceMM = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM));
                        iconPath = cursor.getString(cursor.getColumnIndex(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH));


                        ResourceItem rI = new ResourceItem(id, resouceEng, resouceMM, iconPath);
                        ResourceItems.add(rI);
                        i++;
                    } while (cursor.moveToNext());
                }
                cursor.close();

                if (ResourceItems.size() > 0) {
                    try {
                        //Log.e("Set Up adapter ResourceItems", "===>" + ResourceItems.size());

                        //storageUtil.SaveArrayListToSD("ResourceArrayList", ResourceItems);
                        adapter = new ResourcesListViewAdapter(getActivity().getApplicationContext(), ResourceItems, mstr_lang);
                        lvResouces.setAdapter(adapter);



                    } catch (NullPointerException ex) {
                        ex.printStackTrace();
                    }
                } else {

                    mProgressDialog.dismiss();
                }

            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }

        } else {
            Log.e("ResourceFragment", "Activity Null Case");
        }
    }

    private void getResourceDataFromSever() {
        if (Connection.isOnline(mContext)) {

            Cursor cursorMain = getActivity().getContentResolver().query(IwomenProviderData.ResourceProvider.CONTETN_URI, null, null, null, BaseColumns._ID + " DESC");

            if (cursorMain.getCount() > 0) {

                Log.e("Resource Row Count", "==>" + cursorMain.getCount());

                //skipLimit = skipLimit + 10; // OLd way
                skipLimit = cursorMain.getCount();// my way

                Log.e("Resource Offset  Count", "==>" + offsetlimit + "/" + skipLimit);
                mProgressDialog.show();//{"isAllow": true}
                String sCondition = "{\"isAllow\": true}";
                mProgressDialog.show();
                ResourceAPI.getInstance().getService().getResourceList("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");

                            String id;
                            String resouceEng;
                            String resouceMM;
                            String iconPath;
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);
                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {

                                    id = each_object.getString("objectId");


                                } else {
                                    id = "";
                                }

                                if (!each_object.isNull("resource_title_eng")) {

                                    resouceEng = each_object.getString("resource_title_eng");

                                } else {
                                    resouceEng = "";
                                }
                                if (!each_object.isNull("resource_title_mm")) {

                                    resouceMM = each_object.getString("resource_title_mm");

                                } else {
                                    resouceMM = "";
                                }

                                if (!each_object.isNull("resource_icon_img")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("resource_icon_img");
                                    if (!imgjsonObject.isNull("url")) {

                                        iconPath = imgjsonObject.getString("url");
                                    } else {
                                        iconPath = "";
                                    }


                                } else {
                                    iconPath = "";
                                }

                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID, id);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG, resouceEng);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM, resouceMM);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH, iconPath);



                                cv.put(TableAndColumnsName.ResourceUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.ResourceUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.ResourceUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                Log.e("saveResourceLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.ResourceProvider.CONTETN_URI, cv);

                            }


                            setupAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                        mProgressDialog.dismiss();
                    }
                });

            } else {
                mProgressDialog.show();
                Log.e("First Time Offset Range Count", "==>" + offsetlimit + "/" + skipLimit);//where={"isAllow": true}

                String sCondition = "{\"isAllow\": true}";
                mProgressDialog.show();
                ResourceAPI.getInstance().getService().getResourceList("createdAt", offsetlimit, skipLimit, sCondition, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {

                        try {

                            JSONObject whole_body = new JSONObject(s);
                            JSONArray result = whole_body.getJSONArray("results");

                            String id;
                            String resouceEng;
                            String resouceMM;
                            String iconPath;


                            for (int i = 0; i < result.length(); i++) {
                                JSONObject each_object = result.getJSONObject(i);

                                final ContentValues cv = new ContentValues();
                                if (!each_object.isNull("objectId")) {

                                    id = each_object.getString("objectId");

                                } else {
                                    id = "";
                                }

                                if (!each_object.isNull("resource_title_eng")) {

                                    resouceEng = each_object.getString("resource_title_eng");

                                } else {
                                    resouceEng = "";
                                }
                                if (!each_object.isNull("resource_title_mm")) {

                                    resouceMM = each_object.getString("resource_title_mm");

                                } else {
                                    resouceMM = "";
                                }

                                if (!each_object.isNull("resource_icon_img")) {

                                    JSONObject imgjsonObject = each_object.getJSONObject("resource_icon_img");
                                    if (!imgjsonObject.isNull("url")) {

                                        iconPath = imgjsonObject.getString("url");
                                    } else {
                                        iconPath = "";
                                    }


                                } else {
                                    iconPath = "";
                                }


                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_OBJ_ID, id);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_ENG, resouceEng);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_TITLE_MM, resouceMM);
                                cv.put(TableAndColumnsName.ResourceUtil.RESOURCE_LOGO_IMG_PATH, iconPath);



                                cv.put(TableAndColumnsName.ResourceUtil.STATUS, "0");
                                cv.put(TableAndColumnsName.ResourceUtil.CREATED_DATE, each_object.get("createdAt").toString());// post.get("postUploadedDate").toString() //post.getCreatedAt().toString()
                                cv.put(TableAndColumnsName.ResourceUtil.UPDATED_DATE, each_object.get("updatedAt").toString());


                                //Log.e("SaveResourceLocal : ", "= = = = = = = : " + cv.toString());


                                getActivity().getContentResolver().insert(IwomenProviderData.ResourceProvider.CONTETN_URI, cv);

                            }


                            setupAdapter();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("JSON err", "==>" + e.toString());
                        }
                        mProgressDialog.dismiss();

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("error", "==" + error);
                        mProgressDialog.dismiss();
                    }
                });

            }


        } else {
            //Utils.doToast(mContext, "Internet Connection need!");

            if (mstr_lang.equals(Utils.ENG_LANG)) {
                Utils.doToastEng(mContext, getResources().getString(R.string.open_internet_warning_eng));
            } else {

                Utils.doToastMM(mContext, getActivity().getResources().getString(R.string.open_internet_warning_mm));
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.refresh_menu, menu);
        this.menu = menu;
        this.menu.findItem(R.id.action_rating).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_refresh:
                getResourceDataFromSever();
                return true;
            case R.id.action_rating:
                showReviewDetailDialog();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getReview(){
        NetworkEngine.getInstance().getReview("Be Knowledgeable", new Callback<Rating>() {


            @Override
            public void success(Rating arg0, Response response) {
                menu.findItem(R.id.action_rating).setVisible(true);
                menu.findItem(R.id.action_rating).setIcon(BaseActionBarActivity.getRatingIcon(arg0.getTotalRatings()));
                avgRatings = arg0;
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void showReviewDetailDialog(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        View convertView = View.inflate(getActivity(),R.layout.dialog_reviews_detial,null);
        TextView txt_avg_title = (TextView ) convertView.findViewById(R.id.txt_avg_title);
        TextView txt_total_rating = (TextView ) convertView.findViewById(R.id.txt_total_rating);
        RatingBar avg_ratings = (RatingBar) convertView.findViewById(R.id.avg_ratings);
        TextView txt_avg_ratings = (TextView) convertView.findViewById(R.id.txt_avg_ratings);
        TextView txt_rating_desc = (TextView) convertView.findViewById(R.id.txt_rating_desc);
        Button btn_ok = (Button)convertView.findViewById(R.id.btn_ok);
        alertDialog.setView(convertView);

        txt_avg_title.setText("Overall Rating Be Knowledgeable");
        txt_total_rating.setText(avgRatings.getTotalRatings()+"");
        avg_ratings.setRating(avgRatings.getTotalRatings().floatValue());
        txt_rating_desc.setText(BaseActionBarActivity.getRatingDesc(avgRatings.getTotalRatings()));
        txt_avg_ratings.setText(avgRatings.getTotalUsers()+" Total");

        final AlertDialog ad = alertDialog.show();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
    }





}

