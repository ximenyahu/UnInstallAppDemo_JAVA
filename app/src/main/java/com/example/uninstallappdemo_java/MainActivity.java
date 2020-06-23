package com.example.uninstallappdemo_java;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    //Initialize variable
    private ListView mListView;
    private List<MainData> mDataList = new ArrayList<>();
    private PackageManager mPackageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Assign variable
        mListView = findViewById(R.id.list_view);

        mPackageManager = getPackageManager();
        List<ApplicationInfo> applicationInfoList = mPackageManager.getInstalledApplications(
                PackageManager.GET_META_DATA);
        //use for loop
        for (ApplicationInfo info : applicationInfoList) {
            //check condition
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //only external app will be displayed
                //Initialize main data
                MainData mainData = new MainData();
                mainData.setAppName(info.loadLabel(mPackageManager).toString());
                mainData.setPackageName(info.packageName);
                mainData.setAppIcon(info.loadIcon(mPackageManager));
                //add data in list
                mDataList.add(mainData);
            }
        }

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mDataList.size();
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = getLayoutInflater().inflate(R.layout.list_row_item, null);
                MainData mainData = mDataList.get(position);
                ImageView ivIcon = view.findViewById(R.id.iv_app_icon);
                TextView tvName = view.findViewById(R.id.tv_app_name);
                Button btUninstall = view.findViewById(R.id.bt_uninstall);

                ivIcon.setImageDrawable(mainData.getAppIcon());
                tvName.setText(mainData.getAppName());
                btUninstall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "onClick: ");
                        MainData mainData = mDataList.get(position);
                        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.setData(Uri.parse("package:" + mainData.getPackageName()));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                        startActivityForResult(intent, 100);
                    }
                });
                return view;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
        }
    }
}