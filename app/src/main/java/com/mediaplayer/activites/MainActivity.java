package com.mediaplayer.activites;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mediaplayer.R;
import com.mediaplayer.services.FileSearch;
import com.mediaplayer.services.MobileArrayAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Long> FileSize = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        System.out.println("External Storage::::: " + rawExternalStorage);
        SearchFile(new File(rawExternalStorage));
        ImageButton backImgBtn = (ImageButton) findViewById(R.id.back_btn);
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void SearchFile(File dir) {
        FileSearch objFileSearch = new FileSearch();
        objFileSearch.ctx = this;
        objFileSearch.searchVideoFile(dir);
        List<String> thumb = objFileSearch.thumb;
        final List<String> FileList = objFileSearch.FileList;
        final List<String> video_path = objFileSearch.video_path;
        if (thumb.size() != 0) {
            List<Long> duration = getVideoDuration(video_path, FileList);
            ListView listView = new ListView(this);
            listView.setAdapter(new MobileArrayAdapter(this, FileList, thumb, duration, FileSize));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String fileName = video_path.get(position) + FileList.get(position).toString();
                    Intent intent = new Intent(MainActivity.this, PlayFile.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("fileName", fileName); // value for another activities
                    bundle.putString("videoFileName", FileList.get(position));
                    bundle.putStringArrayList("allVideoPath", (ArrayList<String>) video_path);
                    bundle.putStringArrayList("allFileList", (ArrayList<String>) FileList);
                    intent.putExtras(bundle); // bundle saved as extras
                    startActivity(intent);
                }
            });
            LinearLayout lnrlayout = (LinearLayout) findViewById(R.id.lnrLayout);
            lnrlayout.addView(listView);
        }
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    public List<Long> getVideoDuration(List<String> videoPath, List<String> FileList) {
        List<Long> timeDuration = new ArrayList<>();
        for (int i = 0; i < videoPath.size(); i++) {
            File file = new File(videoPath.get(i) + FileList.get(i).toString());
            FileSize.add(file.length());
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(videoPath.get(i) + FileList.get(i).toString());
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//            MediaPlayer mp = MediaPlayer.create(this, Uri.parse(videoPath.get(i) + FileList.get(i).toString()));
//            int duration = mp.getDuration();
//            System.out.println("File duration::::: " + );
            timeDuration.add(Long.valueOf(Integer.parseInt(time)));
        }
        return timeDuration;
    }

}
