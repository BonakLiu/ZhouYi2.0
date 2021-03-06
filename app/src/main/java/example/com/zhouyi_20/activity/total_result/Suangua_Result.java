package example.com.zhouyi_20.activity.total_result;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import example.com.zhouyi_20.R;
import example.com.zhouyi_20.tool.Birth;
import example.com.zhouyi_20.tool.JsonService;
import example.com.zhouyi_20.tool.TianGanDiZhi;

public class Suangua_Result extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private LinearLayout temp_total;

    private FragmentAdapter fragmentAdapter;
    private example.com.zhouyi_20.activity.total_result.liuyao_fragment_1 liuyao_fragment_1;
    private example.com.zhouyi_20.activity.total_result.liuyao_fragment_2 liuyao_fragment_2;
    private example.com.zhouyi_20.activity.total_result.liuyao_fragment_3 liuyao_fragment_3;

    private LinearLayout point_position;

    private final String urlStr = "http://120.76.128.110:8081/table/item";
    private String postStr;
    private String postDate;//19/5/17新增，传数据要把日期一起传
    private String liuyaoData;
    private LiuYaoMSGHandler handler;
    private LinearLayout zhuanggua_layout;
    private LinearLayout biangua_layout;
    private LinearLayout fushen_layout;

    //变卦的本表
    private LinearLayout bg_layout_1;
    private LinearLayout bg_layout_2;
    private LinearLayout bg_layout_3;
    private LinearLayout bg_layout_4;
    private LinearLayout bg_layout_5;
    private LinearLayout bg_layout_6;
    //变卦的亲表
    private TextView bg_qin_1;
    private TextView bg_qin_2;
    private TextView bg_qin_3;
    private TextView bg_qin_4;
    private TextView bg_qin_5;
    private TextView bg_qin_6;
    //伏神的本表
    private LinearLayout fs_layout_1;
    private LinearLayout fs_layout_2;
    private LinearLayout fs_layout_3;
    private LinearLayout fs_layout_4;
    private LinearLayout fs_layout_5;
    private LinearLayout fs_layout_6;
    //伏神的亲表
    private TextView fs_qin_1;
    private TextView fs_qin_2;
    private TextView fs_qin_3;
    private TextView fs_qin_4;
    private TextView fs_qin_5;
    private TextView fs_qin_6;
    //点一点变一变里面应该展现的项
    private List biangua_show;
    private List biangua_kong;
    private List fushen_show;

    ArrayList<LinearLayout> bg_List;
    ArrayList<TextView> bg_qin;

    ArrayList<LinearLayout> fs_List;
    ArrayList<TextView> fs_qin;

    //装卦部分的点击计数器
    int zhuanggua_count;
    //变卦部分的点击计数器
    int biangua_count;
    //伏神部分的点击计数器
    int fushen_count;

    //空表
    ArrayList<TextView> zg_kong;
    ArrayList<TextView> fs_kong;

    //装卦的空部分的文本框
    private TextView zg_kong_1;
    private TextView zg_kong_2;
    private TextView zg_kong_3;
    private TextView zg_kong_4;
    private TextView zg_kong_5;
    private TextView zg_kong_6;
    //装卦的下部文本框（会变的那个）
    ArrayList<TextView> zg_bian_list;
    private TextView zg_bian_1;
    private TextView zg_bian_2;
    private TextView zg_bian_3;
    private TextView zg_bian_4;
    private TextView zg_bian_5;
    private TextView zg_bian_6;
    //变卦的地支的文本框
    ArrayList<TextView> bg_dizhi_list;
    private TextView bg_dizhi_1;
    private TextView bg_dizhi_2;
    private TextView bg_dizhi_3;
    private TextView bg_dizhi_4;
    private TextView bg_dizhi_5;
    private TextView bg_dizhi_6;
    //伏神的空部分的文本框
    private TextView fs_kong_1;
    private TextView fs_kong_2;
    private TextView fs_kong_3;
    private TextView fs_kong_4;
    private TextView fs_kong_5;
    private TextView fs_kong_6;
    //伏神的地支的文本框
    ArrayList<TextView> fs_dizhi_list;
    private TextView fs_dizhi_1;
    private TextView fs_dizhi_2;
    private TextView fs_dizhi_3;
    private TextView fs_dizhi_4;
    private TextView fs_dizhi_5;
    private TextView fs_dizhi_6;

    //回克逻辑的文本框
    ArrayList<TextView> hk_list;
    private TextView hk_1;
    private TextView hk_2;
    private TextView hk_3;
    private TextView hk_4;
    private TextView hk_5;
    private TextView hk_6;


    // 懒得重新解析因此直接存储
    private Integer liuyaoIntegerData[];

    private final int MSG_GET_DATA_FROM_SERVER_SUCCEED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_result);


        biangua_show = new ArrayList();
        fushen_show = new ArrayList();

        handler = new LiuYaoMSGHandler();

        point_position = (LinearLayout) findViewById(R.id.point_position);

        getDataFromLiuYao();
        getDataFromServer();

        initialize();
        click_init();


    }

    //控件的初始化
    private void initialize() {
        zhuanggua_layout = (LinearLayout) findViewById(R.id.liuyaoresult_zhuanggua_total);
        biangua_layout = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_total);
        fushen_layout = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_total);

        bg_layout_1 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_1);
        bg_layout_2 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_2);
        bg_layout_3 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_3);
        bg_layout_4 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_4);
        bg_layout_5 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_5);
        bg_layout_6 = (LinearLayout) findViewById(R.id.liuyaoresult_biangua_6);

        bg_List = new ArrayList<>();
        bg_List.add(bg_layout_1);
        bg_List.add(bg_layout_2);
        bg_List.add(bg_layout_3);
        bg_List.add(bg_layout_4);
        bg_List.add(bg_layout_5);
        bg_List.add(bg_layout_6);

        bg_qin_1 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_1);
        bg_qin_2 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_2);
        bg_qin_3 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_3);
        bg_qin_4 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_4);
        bg_qin_5 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_5);
        bg_qin_6 = (TextView) findViewById(R.id.liuyaoresult_liuqin_2_6);

        bg_qin = new ArrayList<>();
        bg_qin.add(bg_qin_1);
        bg_qin.add(bg_qin_2);
        bg_qin.add(bg_qin_3);
        bg_qin.add(bg_qin_4);
        bg_qin.add(bg_qin_5);
        bg_qin.add(bg_qin_6);


        fs_layout_1 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_1);
        fs_layout_2 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_2);
        fs_layout_3 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_3);
        fs_layout_4 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_4);
        fs_layout_5 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_5);
        fs_layout_6 = (LinearLayout) findViewById(R.id.liuyaoresult_fushen_6);

        fs_List = new ArrayList<>();
        fs_List.add(fs_layout_1);
        fs_List.add(fs_layout_2);
        fs_List.add(fs_layout_3);
        fs_List.add(fs_layout_4);
        fs_List.add(fs_layout_5);
        fs_List.add(fs_layout_6);

        fs_qin_1 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_1);
        fs_qin_2 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_2);
        fs_qin_3 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_3);
        fs_qin_4 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_4);
        fs_qin_5 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_5);
        fs_qin_6 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_6);

        fs_qin = new ArrayList<>();
        fs_qin.add(fs_qin_1);
        fs_qin.add(fs_qin_2);
        fs_qin.add(fs_qin_3);
        fs_qin.add(fs_qin_4);
        fs_qin.add(fs_qin_5);
        fs_qin.add(fs_qin_6);

        zg_kong_1 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_1);
        zg_kong_2 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_2);
        zg_kong_3 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_3);
        zg_kong_4 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_4);
        zg_kong_5 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_5);
        zg_kong_6 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_6);

        zg_kong = new ArrayList<>();
        zg_kong.add(zg_kong_1);
        zg_kong.add(zg_kong_2);
        zg_kong.add(zg_kong_3);
        zg_kong.add(zg_kong_4);
        zg_kong.add(zg_kong_5);
        zg_kong.add(zg_kong_6);

        zg_bian_1 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        zg_bian_2 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        zg_bian_3 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        zg_bian_4 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        zg_bian_5 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        zg_bian_6 = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);

        zg_bian_list = new ArrayList<>();
        zg_bian_list.add(zg_bian_1);
        zg_bian_list.add(zg_bian_2);
        zg_bian_list.add(zg_bian_3);
        zg_bian_list.add(zg_bian_4);
        zg_bian_list.add(zg_bian_5);
        zg_bian_list.add(zg_bian_6);


        //伏神部分的空表因为后端返回index逻辑好像没有做完，暂时不能够实现
        fs_kong_1 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_1);
        fs_kong_2 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_2);
        fs_kong_3 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_3);
        fs_kong_4 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_4);
        fs_kong_5 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_5);
        fs_kong_6 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_6);

        fs_kong = new ArrayList<>();
        fs_kong.add(fs_kong_1);
        fs_kong.add(fs_kong_2);
        fs_kong.add(fs_kong_3);
        fs_kong.add(fs_kong_4);
        fs_kong.add(fs_kong_5);
        fs_kong.add(fs_kong_6);

        fs_dizhi_1 = (TextView) findViewById(R.id.liuyaoresult_fushen_1_1);
        fs_dizhi_2 = (TextView) findViewById(R.id.liuyaoresult_fushen_2_1);
        fs_dizhi_3 = (TextView) findViewById(R.id.liuyaoresult_fushen_3_1);
        fs_dizhi_4 = (TextView) findViewById(R.id.liuyaoresult_fushen_4_1);
        fs_dizhi_5 = (TextView) findViewById(R.id.liuyaoresult_fushen_5_1);
        fs_dizhi_6 = (TextView) findViewById(R.id.liuyaoresult_fushen_6_1);

        fs_dizhi_list = new ArrayList<>();
        fs_dizhi_list.add(fs_dizhi_1);
        fs_dizhi_list.add(fs_dizhi_2);
        fs_dizhi_list.add(fs_dizhi_3);
        fs_dizhi_list.add(fs_dizhi_4);
        fs_dizhi_list.add(fs_dizhi_5);
        fs_dizhi_list.add(fs_dizhi_6);

        hk_1 = (TextView) findViewById(R.id.huike_text_1);
        hk_2 = (TextView) findViewById(R.id.huike_text_2);
        hk_3 = (TextView) findViewById(R.id.huike_text_3);
        hk_4 = (TextView) findViewById(R.id.huike_text_4);
        hk_5 = (TextView) findViewById(R.id.huike_text_5);
        hk_6 = (TextView) findViewById(R.id.huike_text_6);

        hk_list = new ArrayList<>();
        hk_list.add(hk_1);
        hk_list.add(hk_2);
        hk_list.add(hk_3);
        hk_list.add(hk_4);
        hk_list.add(hk_5);
        hk_list.add(hk_6);

        bg_dizhi_1 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_1);
        bg_dizhi_2 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_2);
        bg_dizhi_3 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_3);
        bg_dizhi_4 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_4);
        bg_dizhi_5 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_5);
        bg_dizhi_6 = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_6);

        bg_dizhi_list = new ArrayList<>();
        bg_dizhi_list.add(bg_dizhi_1);
        bg_dizhi_list.add(bg_dizhi_2);
        bg_dizhi_list.add(bg_dizhi_3);
        bg_dizhi_list.add(bg_dizhi_4);
        bg_dizhi_list.add(bg_dizhi_5);
        bg_dizhi_list.add(bg_dizhi_6);


    }

    //变卦控件可视性的初始化
    private void init_visible_biangua() {

        for (int i = 0; i < 6; i++) {
            bg_List.get(i).setVisibility(View.INVISIBLE);
            bg_qin.get(i).setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < biangua_show.size(); i++) {
            int num = Integer.parseInt(biangua_show.get(i).toString());
            bg_List.get(num).setVisibility(View.VISIBLE);
            bg_qin.get(num).setVisibility(View.VISIBLE);
        }
    }

    //伏神控件可视性的初始化
    private void init_visible_fushen() {

        Log.e("FFFF", fushen_show.toString());
        for (int i = 0; i < 6; i++) {
            fs_List.get(i).setVisibility(View.INVISIBLE);
            fs_qin.get(i).setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < fushen_show.size(); i++) {
            int num = Integer.parseInt(fushen_show.get(i).toString());
            fs_List.get(num).setVisibility(View.VISIBLE);
            fs_qin.get(num).setVisibility(View.VISIBLE);
        }
    }

    //点击事件的初始化
    private void click_init() {
        biangua_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (biangua_count > 1) {
                    biangua_count = 0;
                }
                if (biangua_show.size() != 0) {
                    switch (biangua_count) {
                        case 1:
                            init_visible_biangua();
                            biangua_count++;
                            break;
                        case 0:
                            for (int i = 0; i < 6; i++) {
                                bg_List.get(i).setVisibility(View.VISIBLE);
                                bg_qin.get(i).setVisibility(View.VISIBLE);
                            }
                            biangua_count++;
                            break;
                        default:
                            break;
                    }
                }
            }

        });
//这里就是装挂6列变换的逻辑了
        zhuanggua_layout.setOnClickListener(new View.OnClickListener() {
            //装卦表的点击跳转
            @Override
            public void onClick(View v) {
                if (zhuanggua_count > 5) {
                    zhuanggua_count = 0;
                }
                JsonService jsonInstance = JsonService.getInstance();
                jsonInstance.createJsonObject(liuyaoData);
                String temp;
                JSONArray temp1;//column类型是jsonArray，这里转成String用
                int temp2[] = {0, 0, 0, 0, 0, 0};
                int temp3[] = {0, 0, 0, 0, 0, 0};
                switch (zhuanggua_count) {
                    case 5://回到第一列
                        temp = jsonInstance.getZhuangGua1();
                        printWuXing_zhuanggua(temp);
                        temp = jsonInstance.getBianGua1();
                        printWuxing_biangua(temp);
                        temp = jsonInstance.getFuShen1();
                        printWuxing_fushen(temp);
                        zhuanggua_count++;
                        break;
                    case 0:
                        try {
                            temp1 = jsonInstance.getZhuangGua2();
                            temp = temp1.toString();
                            printZhuanggua_2(temp);
                            temp1 = jsonInstance.getBianGua2();
                            temp = temp1.toString();
                            printBiangua_2(temp);
                            temp1 = jsonInstance.getFuShen2();
                            temp = temp1.toString();
                            printFushen_2(temp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        zhuanggua_count++;
                        break;
                    case 1:
                        try {
                            temp1 = jsonInstance.getZhuangGua3();
                            temp = temp1.toString();
                            printZhuanggua_3(temp);
                            temp1 = jsonInstance.getBianGua3();
                            temp = temp1.toString();
                            printBiangua_3(temp);
                            temp1 = jsonInstance.getFuShen3();
                            temp = temp1.toString();
                            printFushen_3(temp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        zhuanggua_count++;
                        break;
                    case 2:
                        try {
                            temp1 = jsonInstance.getZhuangGua4();
                            Integer a = 1;//用于比较
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printZhuanggua_4(temp2);
                            temp1 = jsonInstance.getBianGua4();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printBiangua_4(temp2);
                            temp1 = jsonInstance.getFuShen4();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp3[i] = 1;
                                }
                            }
                            printFushen_4(temp3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        zhuanggua_count++;
                        break;
                    case 3:
                        try {                            
                            temp1 = jsonInstance.getZhuangGua5();
                            Integer a = 1;//用于比较
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printZhuanggua_5(temp2);
                            temp1 = jsonInstance.getBianGua5();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printBiangua_5(temp2);
                            temp1 = jsonInstance.getFuShen5();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp3[i] = 1;
                                }
                            }
                            printFushen_5(temp3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        zhuanggua_count++;
                        break;
                    case 4:
                        try {
                            temp1 = jsonInstance.getZhuangGua6();
                            Integer a = 1;//用于比较
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printZhuanggua_6(temp2);
                            temp1 = jsonInstance.getBianGua6();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp2[i] = 1;
                                }
                            }
                            printBiangua_6(temp2);
                            temp1 = jsonInstance.getFuShen6();
                            for (int i = 0; i < 6; i++) {
                                if (temp1.get(i) == a) {
                                    temp3[i] = 1;
                                }
                            }
                            printFushen_6(temp3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        zhuanggua_count++;
                        break;
                    default:
                        break;
                }
            }
        });


        fushen_layout.setOnClickListener(new View.OnClickListener() {
            //伏神表点击的显示与不显示
            @Override
            public void onClick(View v) {
                if (fushen_count > 1) {
                    fushen_count = 0;
                }
                switch (fushen_count) {
                    case 1:
                        init_visible_fushen();
                        fushen_count++;
                        break;
                    case 0:
                        for (int i = 0; i < 6; i++) {
                            fs_List.get(i).setVisibility(View.VISIBLE);
                            fs_qin.get(i).setVisibility(View.VISIBLE);
                        }
                        fushen_count++;
                        break;
                    default:
                        break;

                }
            }

        });
    }

    private void paintData() {
        Log.d("LiuYaoResult", liuyaoData);

        JsonService jsonInstance = JsonService.getInstance();
        jsonInstance.createJsonObject(liuyaoData);

        String temp;
        try {
            //表头
            temp = jsonInstance.getContent_zhuanggua();
            if (LiuHe(temp)) {
                printZG_Title();
            }
            temp = jsonInstance.getContent_biangua();
            if (LiuHe(temp)) {
                printBG_Title();
            }
            //伏神的表头逻辑被删除了
//            temp=jsonInstance.getContent_fushen();
//            if(LiuHe(temp)){
//                printFS_Title();
//            }

            temp = jsonInstance.getTimes_zhuanggua();
            ZG_title_times(temp);

            temp = jsonInstance.getTimes_biangua();
            BG_title_times(temp);

//            temp = jsonInstance.getTimes_fushen();
//            FS_title_times(temp);


            // 从左往右第1列六亲
            temp = jsonInstance.getLiuqin_1();
            printLiuQin1(temp);
//            // 从左往右第2列六亲
//            temp=jsonInstance.getLiuqin_2();
//            printLiuQin2(temp);
            // 从左往右第3列六亲
            temp = jsonInstance.getLiuqin_3();
            printLiuQin3(temp);
            // 六爻卦象的图片
            printLiuYaoImage();
            // 世应
            temp = jsonInstance.getShiying();
            printShiying(temp);
            // 六兽
            printLiuShou();
            // 日期的格子
            printDate();
            //五行(装卦表里的部分)
            temp = jsonInstance.getZhuangGua1();
            printWuXing_zhuanggua(temp);
            //Heaven表
            temp = jsonInstance.getHeavenly_stems();
            printHeaven(temp);
            temp = jsonInstance.getHeavenly_stems_biangua();
            printHeaven_biangua(temp);
            temp = jsonInstance.getHeavenly_stems_fushen();
            printHeaven_fushen(temp);
            //Earthly表
            temp = jsonInstance.getEarthly_branches();
            printEarthly(temp);
            temp = jsonInstance.getEarthly_biangua();
            printEarthly_biangua(temp);
            temp = jsonInstance.getEarthly_fushen();
            printEarthly_fushen(temp);


            //五行（伏神表里的部分）
            temp = jsonInstance.getFuShen1();
            printWuxing_fushen(temp);

            //五行（变卦表里的部分）
            temp = jsonInstance.getBianGua1();
            printWuxing_biangua(temp);

//            //日表
            String day_String = jsonInstance.getDaytable();
            //变表
            List bian_list = jsonInstance.getBiantable();
            //亲表（下端部分）
            List qin_list = jsonInstance.getQintable();


            //content
            //bengui
            String ben_String = jsonInstance.getContent_zhuanggua();

            //biangua
            String bian_String = jsonInstance.getContent_biangua();
            //fushen
            String fushen_String = jsonInstance.getContent_fushen();
            //shen_gua
            String shen_gua = jsonInstance.getShen_gua();

            Bundle bundle = new Bundle();
            bundle.putString("day_string", day_String);
            bundle.putStringArrayList("qin_string", (ArrayList<String>) qin_list);
            bundle.putStringArrayList("bian_table", (ArrayList<String>) bian_list);
            bundle.putString("ben_string", ben_String);
            bundle.putString("bian_string", bian_String);
            bundle.putString("shou_string", fushen_String);
            bundle.putString("shen_gua", shen_gua);
//


            //kong部分
            String kong_table = "";
            List list_zg = jsonInstance.getKong_zhuanggua();
            for (int i = 0; i < list_zg.size(); i++) {
                int num = Integer.parseInt(list_zg.get(i).toString());
                zg_kong.get(num).setBackgroundResource(R.drawable.right_triangle);
            }
            List list_dizhi = jsonInstance.getKong_dizhi();
            for (int i = 0; i < list_dizhi.size(); i++) {
                kong_table += list_dizhi.get(i);
            }
            bundle.putString("kong_table", kong_table);


            List list_bg = jsonInstance.getKong_biangua();
            for (int i = 0; i < list_bg.size(); i++) {
                int num = Integer.parseInt(list_bg.get(i).toString());
                bg_qin.get(num).setBackgroundResource(R.drawable.small_triangle);
            }
            Log.e("bi_kong", list_bg.toString());
            
            List list_fs = jsonInstance.getKong_fushen();
            for (int i = 0; i < list_fs.size(); i++) {
                int num = Integer.parseInt(list_fs.get(i).toString());
                fs_qin.get(num).setBackgroundResource(R.drawable.small_triangle);
            }
            
            //回克逻辑
            printHuiKe();
            printDateQin();

            //装卦的六亲
            printLiuQin2();

            fragment_init();
            liuyao_fragment_2.setArguments(bundle);
            fragment_manage();

//
//            //事由
//
//            temp=NewRecord.shiyou_string;
//            printShiyou(temp);

            //show 部分

            List list_bg_show = jsonInstance.getShow_biangua();
            if (!list_bg_show.isEmpty()) {
                for (int i = 0; i < list_bg_show.size(); i++) {
                    biangua_show.add(list_bg_show.get(i).toString());
                }
            }


            List list_fs_show = jsonInstance.getShow_fushen();
            if (!list_fs_show.isEmpty()) {
                for (int i = 0; i < list_fs_show.size(); i++) {
                    fushen_show.add(list_fs_show.get(i).toString());
                }
            }
            Log.e("FF", fushen_show.toString());


//            //神表
//            printShenTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fragment_init() {
        fragmentList.clear();
        viewPager = (ViewPager) findViewById(R.id.liuyao_bottom);
        liuyao_fragment_1 = new liuyao_fragment_1();
        liuyao_fragment_2 = new liuyao_fragment_2();
        liuyao_fragment_3 = new liuyao_fragment_3();
        fragmentList.add(liuyao_fragment_1);
        fragmentList.add(liuyao_fragment_2);
        fragmentList.add(liuyao_fragment_3);

        //初始时刻生成三个点
        int page = fragmentList.size();
        for (int i = 0; i < page; i++) {
            Change_Point point = new Change_Point(Suangua_Result.this);
            //将第二个点变为选中颜色
            if (i == 1) {
                point.setSelected(true);
            } else {
                point.setSelected(false);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
            params.leftMargin = 0;
            params.topMargin = 0;
            point_position.addView(point, params);
        }
    }

    private void fragment_manage() {
        fragmentAdapter = new FragmentAdapter(this.getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //在每次选中更改的时候，清空
                point_position.removeAllViews();
                //清空后画点
                int page = fragmentList.size();
                for (int i = 0; i < page; i++) {
                    Change_Point point = new Change_Point(Suangua_Result.this);
                    if (i == position) {
                        point.setSelected(true);
                    } else {
                        point.setSelected(false);
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
                    params.leftMargin = 0;
                    params.topMargin = 0;
                    point_position.addView(point, params);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    // 从liuYaoJinqiangua获取数据并解析
    private void getDataFromLiuYao() {
        Intent intent = getIntent();
        liuyaoIntegerData = (Integer[]) intent.getSerializableExtra("LiuYaoData");
        Collections.reverse(Arrays.asList(liuyaoIntegerData));
        postStr = "[" + liuyaoIntegerData[0];
        for (int i = 1; i < 6; ++i)
            postStr += "," + liuyaoIntegerData[i];
        postStr += "]";
        Log.e("postStr", postStr);
        postDate = intent.getStringExtra("date");//从intent取出date
        postDate += ":00";//只有分，后端要求秒，所以手动加个秒
    }


    // 从服务器端获取数据
    private void getDataFromServer() {
        //下面两句是老方法
        Thread getDataThread = new Thread(new WebGetDataService());
        getDataThread.start();
    }

    // 处理消息
    private class LiuYaoMSGHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_GET_DATA_FROM_SERVER_SUCCEED:
                    paintData();
                    init_visible_biangua();
                    init_visible_fushen();
                    break;
                default:
                    break;
            }
        }
    }

    // Android不允许在UI线程执行的操作
    private class WebGetDataService implements Runnable {
        @Override
        public void run() {
            Looper.prepare();
            try {
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type", "application/json");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(getJsonData().toString().getBytes());//改成这样，经过测试，成功了

                outputStream.close();

                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                    Toast.makeText(Suangua_Result.this, "起卦成功！", Toast.LENGTH_LONG).show();

                    InputStream inputStream = connection.getInputStream();
                    liuyaoData = IOUtils.toString(inputStream, "UTF-8");

                    //成功获取数据，向主线程传递信息
                    Message msg = new Message();
                    msg.what = MSG_GET_DATA_FROM_SERVER_SUCCEED;
                    handler.sendMessage(msg);
                } else
                    Toast.makeText(Suangua_Result.this, "起卦失败，请检查网络", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(Suangua_Result.this, "起卦失败，请检查网络", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Looper.loop();
        }
    }

    /**
     * @return 试着用一般的方式POST
     */
    private JSONObject getJsonData() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 6; i++) {
            jsonArray.put(liuyaoIntegerData[i]);
        }
        try {
            jsonObject.put("list", jsonArray);
            jsonObject.put("time", postDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**************************** 以下函数用于填充数据到UI **************************/

    //表头逻辑
    private boolean LiuHe(String temp) {
        if (temp.equals("天地否") || temp.equals("水泽节") || temp.equals("山火贲") || temp.equals("雷地豫") || temp.equals("火山旅") || temp.equals("地雷复") || temp.equals("地天泰") || temp.equals("泽水困")) {
            return true;
        } else {
            return false;
        }
    }

    private void printZG_Title() {
        TextView textView = (TextView) findViewById(R.id.result_change_title_1);
        textView.setText("六合");
        textView.setTextSize(20);
    }

    private void printBG_Title() {
        TextView textView = (TextView) findViewById(R.id.result_change_title_2);
        textView.setText("六合");
        textView.setTextSize(20);
    }
//    private void printFS_Title(){
//        TextView textView = (TextView)findViewById(R.id.result_change_title_3);
//        textView.setText("六合");
//    }

    private void ZG_title_times(String temp) {
        if (temp.equals("归魂卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_1);
            textView.setText("归魂");
            textView.setTextSize(20);
        }
        if (temp.equals("游魂卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_1);
            textView.setText("游魂");
            textView.setTextSize(20);
        }
        if (temp.equals("本宫卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_1);
            textView.setText("六冲");
            textView.setTextSize(20);
        } else {
            return;
        }
    }

    private void BG_title_times(String temp) {
        if (temp.equals("归魂卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_2);
            textView.setText("归魂");
            textView.setTextSize(20);
        }
        if (temp.equals("游魂卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_2);
            textView.setText("游魂");
            textView.setTextSize(20);
        }
        if (temp.equals("本宫卦")) {
            TextView textView = (TextView) findViewById(R.id.result_change_title_2);
            textView.setText("六冲");
            textView.setTextSize(20);
        } else {
            return;
        }
    }

    private void FS_title_times(String temp) {
//        if(temp.equals("归魂卦")){
//            TextView textView = (TextView)findViewById(R.id.result_change_title_3);
//            textView.setText("归魂");
//        }
//        if(temp.equals("游魂卦")){
//            TextView textView = (TextView)findViewById(R.id.result_change_title_3);
//            textView.setText("游魂");
//        }
//        if(temp.equals("本宫卦")){
//            TextView textView = (TextView)findViewById(R.id.result_change_title_3);
//            textView.setText("六冲");
//        }
//        else {
//            return;
//        }
    }

    // 从左至右第一个六亲
    private void printLiuQin1(String liuqin) {
        //标题
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_caption);
        textView.setText("六亲");

        String temp = liuqin.substring(2, 4);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_1);
        textView.setText(temp);

        temp = liuqin.substring(7, 9);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_2);
        textView.setText(temp);

        temp = liuqin.substring(12, 14);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_3);
        textView.setText(temp);

        temp = liuqin.substring(17, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_4);
        textView.setText(temp);

        temp = liuqin.substring(22, 24);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_5);
        textView.setText(temp);

        temp = liuqin.substring(27, 29);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_1_6);
        textView.setText(temp);
    }

    // 从左至右第二个六亲
    private void printLiuQin2() {
        for (int i = 0; i < 6; i++) {
            TextView textView = bg_dizhi_list.get(i);
            ArrayList<String> testList = new ArrayList();

            if ("丑辰未戌".contains(textView.getText().toString())) {
                testList.clear();
                testList.add("丑辰未戌");
            }
            if ("亥子".contains(textView.getText().toString())) {
                testList.clear();
                testList.add("亥子");
            }
            if ("寅卯".contains(textView.getText().toString())) {
                testList.clear();
                testList.add("寅卯");
            }
            if ("巳午".contains(textView.getText().toString())) {
                testList.clear();
                testList.add("巳午");
            }
            if ("申酉".contains(textView.getText().toString())) {
                testList.clear();
                testList.add("申酉");
            }

            for (int j = 0; j < 6; j++) {
                textView = fs_dizhi_list.get(j);
                if (testList.get(0).contains(textView.getText())) {
                    bg_qin.get(i).setText(fs_qin.get(j).getText());
                }
            }
        }

    }

    // 从左至右第三个六亲
    private void printLiuQin3(String liuqin) {


        String temp = liuqin.substring(2, 4);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_1);
        textView.setText(temp);

        temp = liuqin.substring(7, 9);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_2);
        textView.setText(temp);

        temp = liuqin.substring(12, 14);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_3);
        textView.setText(temp);

        temp = liuqin.substring(17, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_4);
        textView.setText(temp);

        temp = liuqin.substring(22, 24);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_5);
        textView.setText(temp);

        temp = liuqin.substring(27, 29);
        textView = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_6);
        textView.setText(temp);
    }

    // 世应
    private void printShiying(String shiying) {
        TextView textView;
        // 以下高能，完全丢弃了作为一个程序员的节操

        String temp = new String();

        int i = -1;
        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_1);
                textView.setText(temp);
                break;
            }
        }

        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_2);
                textView.setText(temp);
                break;
            }
        }

        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_3);
                textView.setText(temp);
                break;
            }
        }

        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_4);
                textView.setText(temp);
                break;
            }
        }

        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_5);
                textView.setText(temp);
                break;
            }
        }

        while (true) {
            ++i;
            if (shiying.charAt(i) == '[' || shiying.charAt(i) == '"' || shiying.charAt(i) == ',')
                continue;
            else if (Character.isDigit(shiying.charAt(i)))
                break;
            else {
                temp = Character.toString(shiying.charAt(i));
                textView = (TextView) findViewById(R.id.liuyaoresult_shiying_6);
                textView.setText(temp);
                break;
            }
        }
    }

    // 六爻卦象的图片
    private void printLiuYaoImage() {
        ImageView temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_1);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[0]));
        temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_2);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[1]));
        temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_3);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[2]));
        temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_4);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[3]));
        temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_5);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[4]));
        temp = (ImageView) findViewById(R.id.liuyaoresult_gua_image_6);
        temp.setImageResource(returnLiuYaoImageID(liuyaoIntegerData[5]));
    }

    private int returnLiuYaoImageID(int index) {
        switch (index) {
            case 6:
                return R.drawable.g6;
            case 7:
                return R.drawable.g7;
            case 8:
                return R.drawable.g8;
            case 9:
                return R.drawable.g9;
            default:
                return 0;
        }
    }

    // 六兽部分
    private void printLiuShou() throws ParseException {
        String[] liuShou = calculateLiuShou();

        //标题
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_liushen_caption);
        //textView.setText("六兽");此处注释掉，保留六神

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_1);
        textView.setText(liuShou[0]);

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_2);
        textView.setText(liuShou[1]);

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_3);
        textView.setText(liuShou[2]);

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_4);
        textView.setText(liuShou[3]);

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_5);
        textView.setText(liuShou[4]);

        textView = (TextView) findViewById(R.id.liuyaoresult_liushou_6);
        textView.setText(liuShou[5]);
    }

    // 计算六兽数据
    private String[] calculateLiuShou() throws ParseException {
        String[] sixGods = {"青龙", "朱雀", "勾陈", "腾蛇", "白虎", "玄武"};
        String[] result = new String[6];

        // 计算天干日
        Calendar cal = Calendar.getInstance();

        Intent intent = getIntent();
        String time = intent.getStringExtra("date");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = formatter.parse(time);

        cal.setTime(date);
        Birth birth = new Birth(cal);

        Map map = birth.horoscope(time);
        String ganzhi_month = map.get("cD").toString();
        int index;
        if (ganzhi_month.substring(0, 1).equals("甲") || ganzhi_month.substring(0, 1).equals("乙")) {
            index = 0;
        } else if (ganzhi_month.substring(0, 1).equals("丙") || ganzhi_month.substring(0, 1).equals("丁")) {
            index = 1;
        } else if (ganzhi_month.substring(0, 1).equals("戊")) {
            index = 2;
        } else if (ganzhi_month.substring(0, 1).equals("己")) {
            index = 3;
        } else if (ganzhi_month.substring(0, 1).equals("庚") || ganzhi_month.substring(0, 1).equals("辛")) {
            index = 4;
        } else {
            index = 5;
        }
        for (int i = 5; i >= 0; --i) {
            result[i] = sixGods[index++];
            if (index >= 6)
                index = 0;
        }
        return result;
    }

    //     填上表格中所有与日期有关的格子
    private void printDate() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        Intent intent = getIntent();
        String time = intent.getStringExtra("date");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = formatter.parse(time);

        TextView textView;


        // 农历日月年干支用Birth计算
        //五行用TianGanDiZhi计算

        cal.setTime(date);
        Birth birth = new Birth(cal);

        Map map = birth.horoscope(time);
        String ganzhi_year = map.get("cY").toString();
        String ganzhi_month = map.get("cM").toString();
        String ganzhi_day = map.get("cD").toString();
        String ganzhi_hour = map.get("cH").toString();

        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_day_1);
        textView.setText(ganzhi_day.substring(0, 1));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_day_2);
        textView.setText(ganzhi_day.substring(1, 2));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_month_1);
        textView.setText(ganzhi_month.substring(0, 1));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_month_2);
        textView.setText(ganzhi_month.substring(1, 2));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_year_1);
        textView.setText(ganzhi_year.substring(0, 1));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_year_2);
        textView.setText(ganzhi_year.substring(1, 2));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_hour_1);
        textView.setText(ganzhi_hour.substring(0, 1));
        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_hour_2);
        textView.setText(ganzhi_hour.substring(1, 2));


    }

    //五行（装卦表的形态1）
    private void printWuXing_zhuanggua(String wuxing_1) {
        String temp = wuxing_1.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText(temp);

        temp = wuxing_1.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText(temp);

        temp = wuxing_1.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText(temp);

        temp = wuxing_1.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText(temp);

        temp = wuxing_1.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText(temp);

        temp = wuxing_1.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText(temp);

    }

    //填上变卦表里面的五行
    private void printWuxing_biangua(String wuxing_3) {
        String temp = wuxing_3.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText(temp);

        temp = wuxing_3.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText(temp);

        temp = wuxing_3.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText(temp);

        temp = wuxing_3.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText(temp);

        temp = wuxing_3.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText(temp);

        temp = wuxing_3.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText(temp);
    }

    //填上伏神表里面的五行
    private void printWuxing_fushen(String wuxing_2) {
        String temp = wuxing_2.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText(temp);

        temp = wuxing_2.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText(temp);

        temp = wuxing_2.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText(temp);

        temp = wuxing_2.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText(temp);

        temp = wuxing_2.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText(temp);

        temp = wuxing_2.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText(temp);

    }

    //装卦表形态2
    private void printZhuanggua_2(String temp) throws ParseException {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText(temp.substring(22, 23));

    }

    private void printBiangua_2(String temp) throws ParseException {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText(temp.substring(22, 23));
        //拿五行
//        Intent intent = getIntent();
//        String time = intent.getStringExtra("date");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        Date date = formatter.parse(time);
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        TianGanDiZhi ganzhi = new TianGanDiZhi(cal, date);
//        String zhi_month_wuxing[] = ganzhi.getWuXing();
//        //拼装
//        zhi_month_wuxing[0] = zhi_month_wuxing[0] + "旺";
//        zhi_month_wuxing[1] = zhi_month_wuxing[1] + "相";
//        zhi_month_wuxing[2] = zhi_month_wuxing[2] + "死";
//        zhi_month_wuxing[3] = zhi_month_wuxing[3] + "囚";
//        zhi_month_wuxing[4] = zhi_month_wuxing[4] + "休";
//        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
    }

    private void printFushen_2(String temp) throws ParseException {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText(temp.substring(22, 23));
//        Intent intent = getIntent();
//        String time = intent.getStringExtra("date");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        Date date = formatter.parse(time);
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        TianGanDiZhi ganzhi = new TianGanDiZhi(cal, date);
//        String zhi_month_wuxing[] = ganzhi.getWuXing();
//        //拼装
//        zhi_month_wuxing[0] = zhi_month_wuxing[0] + "旺";
//        zhi_month_wuxing[1] = zhi_month_wuxing[1] + "相";
//        zhi_month_wuxing[2] = zhi_month_wuxing[2] + "死";
//        zhi_month_wuxing[3] = zhi_month_wuxing[3] + "囚";
//        zhi_month_wuxing[4] = zhi_month_wuxing[4] + "休";
//        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
//        for (int i = 0; i <= 4; i++) {
//            if (textView.getText().toString().equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
    }

    //装卦表形态3
    private void printZhuanggua_3(String temp) throws ParseException {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText(temp.substring(22, 23));
    }

    private void printBiangua_3(String temp) throws ParseException {
//        //拿五行
//        Intent intent = getIntent();
//        String time = intent.getStringExtra("date");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        Date date = formatter.parse(time);
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        TianGanDiZhi ganzhi = new TianGanDiZhi(cal, date);
//        String zhi_month_wuxing[] = ganzhi.getWuXing();
//        //拿日表
//        JsonService jsonInstance = JsonService.getInstance();
//        jsonInstance.createJsonObject(liuyaoData);
//        String temp = jsonInstance.getDaytable();
//        //拼装
//        zhi_month_wuxing[0] = zhi_month_wuxing[0] + temp.substring(0, 1);
//        zhi_month_wuxing[1] = zhi_month_wuxing[1] + temp.substring(1, 2);
//        zhi_month_wuxing[2] = zhi_month_wuxing[2] + temp.substring(2, 3);
//        zhi_month_wuxing[3] = zhi_month_wuxing[3] + temp.substring(3, 4);
//        zhi_month_wuxing[4] = zhi_month_wuxing[4] + temp.substring(4);
//        //填入
//
//        temp = jsonInstance.getZhuangGua1();
//        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(2, 3).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(6, 7).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(10, 11).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(14, 15).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(18, 19).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(22, 23).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText(temp.substring(22, 23));

    }

    private void printFushen_3(String temp) throws ParseException {
//        //拿五行
//        Intent intent = getIntent();
//        String time = intent.getStringExtra("date");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
//        Date date = formatter.parse(time);
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        TianGanDiZhi ganzhi = new TianGanDiZhi(cal, date);
//        String zhi_month_wuxing[] = ganzhi.getWuXing();
//        //拿日表
//        JsonService jsonInstance = JsonService.getInstance();
//        jsonInstance.createJsonObject(liuyaoData);
//        String temp = jsonInstance.getDaytable();
//        //拼装
//        zhi_month_wuxing[0] = zhi_month_wuxing[0] + temp.substring(0, 1);
//        zhi_month_wuxing[1] = zhi_month_wuxing[1] + temp.substring(1, 2);
//        zhi_month_wuxing[2] = zhi_month_wuxing[2] + temp.substring(2, 3);
//        zhi_month_wuxing[3] = zhi_month_wuxing[3] + temp.substring(3, 4);
//        zhi_month_wuxing[4] = zhi_month_wuxing[4] + temp.substring(4);
//        //填入
//
//        temp = jsonInstance.getZhuangGua1();
//        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(2, 3).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(6, 7).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(10, 11).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(14, 15).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(18, 19).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
//        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
//        for (int i = 0; i <= 4; i++) {
//            if (temp.substring(22, 23).equals(zhi_month_wuxing[i].toString().substring(0, 1))) {
//                textView.setText(zhi_month_wuxing[i].toString().substring(1, 2));
//                break;
//            }
//        }
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText(temp.substring(2, 3));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText(temp.substring(6, 7));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText(temp.substring(10, 11));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText(temp.substring(14, 15));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText(temp.substring(18, 19));
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText(temp.substring(22, 23));

    }

    //装卦表形态4
    private void printZhuanggua_4(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("刑");
        }
    }

    private void printBiangua_4(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("刑");
        }
    }

    private void printFushen_4(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("刑");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("刑");
        }
    }

    //装卦表形态5
    private void printZhuanggua_5(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("冲");
        }
    }

    private void printBiangua_5(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("冲");
        }
    }

    private void printFushen_5(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("冲");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("冲");
        }
    }

    //装卦表形态6
    private void printZhuanggua_6(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_3_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("害");
        }
    }
    private void printBiangua_6(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_1);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_2);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_4);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_5);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_wuxing_6);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("害");
        }
    }

    private void printFushen_6(int[] temp) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_3);
        textView.setText("");
        if (temp[0] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_3);
        textView.setText("");
        if (temp[1] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_3);
        textView.setText("");
        if (temp[2] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_3);
        textView.setText("");
        if (temp[3] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_3);
        textView.setText("");
        if (temp[4] == 1) {
            textView.setText("害");
        }
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_3);
        textView.setText("");
        if (temp[5] == 1) {
            textView.setText("害");
        }
    }

    //填上装卦左边那部分（不知道是什么了）
    private void printHeaven(String heavenly_stems) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_2_1);
        String temp = heavenly_stems.substring(2, 3);
        textView.setText(temp);

        temp = heavenly_stems.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_2_4);
        textView.setText(temp);
    }

    //填上变卦里面类似的部分
    private void printHeaven_biangua(String heavenly_biangua) {
        TextView textView = (TextView) findViewById(R.id.biangua_branches_1);
        String temp = heavenly_biangua.substring(2, 3);
        textView.setText(temp);

        temp = heavenly_biangua.substring(6, 7);
        textView = (TextView) findViewById(R.id.biangua_branches_2);
        textView.setText(temp);
    }

    //填上伏神里面的类似部分
    private void printHeaven_fushen(String hevenly_fushen) {
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_2);
        String temp = hevenly_fushen.substring(2, 3);
        textView.setText(temp);

        temp = hevenly_fushen.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_2);
        textView.setText(temp);
    }

    //填上装卦黄色那部分（再次不知道是什么了）
    private void printEarthly(String earthly_branches) {
        String temp = earthly_branches.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_1);
        textView.setText(temp);

        temp = earthly_branches.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_2);
        textView.setText(temp);

        temp = earthly_branches.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_3);
        textView.setText(temp);

        temp = earthly_branches.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_4);
        textView.setText(temp);

        temp = earthly_branches.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_5);
        textView.setText(temp);

        temp = earthly_branches.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_zhuanggua_1_6);
        textView.setText(temp);
    }

    //填上变卦表类似的部分
    private void printEarthly_biangua(String earthly_biangua) {
        String temp = earthly_biangua.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_1);
        textView.setText(temp);

        temp = earthly_biangua.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_2);
        textView.setText(temp);

        temp = earthly_biangua.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_3);
        textView.setText(temp);

        temp = earthly_biangua.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_4);
        textView.setText(temp);

        temp = earthly_biangua.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_5);
        textView.setText(temp);

        temp = earthly_biangua.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_bg_dizhi_6);
        textView.setText(temp);
    }

    // 填上伏神里面的类似部分
    private void printEarthly_fushen(String earthly_fushen) {
        String temp = earthly_fushen.substring(2, 3);
        TextView textView = (TextView) findViewById(R.id.liuyaoresult_fushen_1_1);
        textView.setText(temp);

        temp = earthly_fushen.substring(6, 7);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_2_1);
        textView.setText(temp);

        temp = earthly_fushen.substring(10, 11);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_3_1);
        textView.setText(temp);

        temp = earthly_fushen.substring(14, 15);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_4_1);
        textView.setText(temp);

        temp = earthly_fushen.substring(18, 19);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_5_1);
        textView.setText(temp);

        temp = earthly_fushen.substring(22, 23);
        textView = (TextView) findViewById(R.id.liuyaoresult_fushen_6_1);
        textView.setText(temp);
    }


    //回克逻辑
    private void printHuiKe() {
        HashMap HuiHe_map = new HashMap();
        HuiHe_map.put("子", "丑");
        HuiHe_map.put("丑", "子");
        HuiHe_map.put("寅", "亥");
        HuiHe_map.put("卯", "戌");
        HuiHe_map.put("辰", "酉");
        HuiHe_map.put("巳", "申");
        HuiHe_map.put("午", "未");
        HuiHe_map.put("未", "午");
        HuiHe_map.put("申", "巳");
        HuiHe_map.put("酉", "辰");
        HuiHe_map.put("戌", "卯");
        HuiHe_map.put("亥", "寅");

        HashMap Ke_map = new HashMap();
        Ke_map.put("子", "巳午");
        Ke_map.put("丑", "子亥");
        Ke_map.put("寅", "丑辰未戌");
        Ke_map.put("卯", "丑辰未戌");
        Ke_map.put("辰", "子亥");
        Ke_map.put("巳", "申酉");
        Ke_map.put("午", "申酉");
        Ke_map.put("未", "子亥");
        Ke_map.put("申", "寅卯");
        Ke_map.put("酉", "寅卯");
        Ke_map.put("戌", "子亥");
        Ke_map.put("亥", "巳午");

        HashMap Sheng_map = new HashMap();
        Sheng_map.put("子", "寅卯");
        Sheng_map.put("丑", "申酉");
        Sheng_map.put("寅", "巳午");
        Sheng_map.put("卯", "巳午");
        Sheng_map.put("辰", "申酉");
        Sheng_map.put("巳", "丑辰");
        Sheng_map.put("午", "丑辰");
        Sheng_map.put("未", "申酉");
        Sheng_map.put("申", "子亥");
        Sheng_map.put("酉", "子亥");
        Sheng_map.put("戌", "申酉");
        Sheng_map.put("亥", "寅卯");

        HashMap Hua_map = new HashMap();
        Hua_map.put(1, "丑辰未戌");
        Hua_map.put(2, "亥子");
        Hua_map.put(3, "寅卯");
        Hua_map.put(4, "巳午");
        Hua_map.put(5, "申酉");

        for (int i = 0; i < 6; i++) {
            hk_list.get(i).setText("");
            Log.e("zhuanggua", bg_dizhi_list.get(i).getText().toString());
            if (HuiHe_map.get(bg_dizhi_list.get(i).getText().toString()).equals(zg_kong.get(i).getText().toString())) {
                Log.e("biangua111", zg_kong.get(i).getText().toString());
                hk_list.get(i).setText("回合");
            } else if ((Ke_map.get(bg_dizhi_list.get(i).getText()).toString()).contains(zg_kong.get(i).getText().toString())) {
                if (hk_list.get(i).equals("回合")) {
                    hk_list.get(i).setText("合克");
                    continue;
                } else {
                    hk_list.get(i).setText("回克");
                    continue;
                }
            } else if ((Sheng_map.get(bg_dizhi_list.get(i).getText()).toString()).contains(zg_kong.get(i).getText().toString())) {
                hk_list.get(i).setText("回生");
                continue;
            }

            for (int j = 1; j < 6; j++) {
                if (Hua_map.get(j).toString().contains(bg_dizhi_list.get(i).getText().toString()) && Hua_map.get(j).toString().contains(zg_kong.get(i).getText().toString())) {
                    if ("戌亥卯午酉".contains(bg_dizhi_list.get(i).getText().toString())) {
                        hk_list.get(i).setText("化进");
                    }
                    if (bg_dizhi_list.get(i).getText().toString().equals("未") && "辰丑".contains(zg_kong.get(i).getText().toString())) {
                        hk_list.get(i).setText("化进");
                    }
                    if (bg_dizhi_list.get(i).getText().toString().equals("辰") && zg_kong.get(i).getText().toString().equals("丑")) {
                        hk_list.get(i).setText("化进");
                    }
                    if (bg_dizhi_list.get(i).getText().toString().equals(zg_kong.get(i).getText().toString())) {
                        hk_list.get(i).setText("");
                    } else {
                        hk_list.get(i).setText("化退");
                    }

                }
            }


        }
    }

    //年月日里的亲表（极其暴力的实现）
    private void printDateQin() {
        ArrayList<TextView> fushen_list = new ArrayList<>();
        ArrayList<TextView> liuqin_list = new ArrayList<>();

        TextView fushen_1, fushen_2, fushen_3, fushen_4, fushen_5, fushen_6;
        TextView liuqin_1, liuqin_2, liuqin_3, liuqin_4, liuqin_5, liuqin_6;
        fushen_1 = (TextView) findViewById(R.id.liuyaoresult_fushen_1_1);
        fushen_2 = (TextView) findViewById(R.id.liuyaoresult_fushen_2_1);
        fushen_3 = (TextView) findViewById(R.id.liuyaoresult_fushen_3_1);
        fushen_4 = (TextView) findViewById(R.id.liuyaoresult_fushen_4_1);
        fushen_5 = (TextView) findViewById(R.id.liuyaoresult_fushen_5_1);
        fushen_6 = (TextView) findViewById(R.id.liuyaoresult_fushen_6_1);

        fushen_list.add(fushen_1);
        fushen_list.add(fushen_2);
        fushen_list.add(fushen_3);
        fushen_list.add(fushen_4);
        fushen_list.add(fushen_5);
        fushen_list.add(fushen_6);

        liuqin_1 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_1);
        liuqin_2 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_2);
        liuqin_3 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_3);
        liuqin_4 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_4);
        liuqin_5 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_5);
        liuqin_6 = (TextView) findViewById(R.id.liuyaoresult_liuqin_3_6);

        liuqin_list.add(liuqin_1);
        liuqin_list.add(liuqin_2);
        liuqin_list.add(liuqin_3);
        liuqin_list.add(liuqin_4);
        liuqin_list.add(liuqin_5);
        liuqin_list.add(liuqin_6);

        TextView textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_year_2);
        TextView temp = (TextView) findViewById(R.id.liuqin_year);
        String part = textView.getText().toString();
        String full = new String();
        switch (part) {
            case "丑":
                full = "丑辰未戌";
                break;
            case "辰":
                full = "丑辰未戌";
                break;
            case "未":
                full = "丑辰未戌";
                break;
            case "戌":
                full = "丑辰未戌";
                break;
            case "亥":
                full = "亥子";
                break;
            case "子":
                full = "亥子";
                break;
            case "寅":
                full = "寅卯";
                break;
            case "卯":
                full = "寅卯";
                break;
            case "巳":
                full = "巳午";
                break;
            case "午":
                full = "巳午";
                break;
            case "申":
                full = "申酉";
                break;
            case "酉":
                full = "申酉";
                break;
            default:
                break;
        }
        for (int i = 0; i < 6; i++) {
            if (full.contains(fushen_list.get(i).getText())) {
                if (liuqin_list.get(i).getText().toString().equals("子孙") || liuqin_list.get(i).getText().toString().equals("妻财")) {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(1, 2));
                } else {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(0, 1));
                }
            }
        }

        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_month_2);
        temp = (TextView) findViewById(R.id.liuqin_month);
        part = textView.getText().toString();
        full = new String();
        switch (part) {
            case "丑":
                full = "丑辰未戌";
                break;
            case "辰":
                full = "丑辰未戌";
                break;
            case "未":
                full = "丑辰未戌";
                break;
            case "戌":
                full = "丑辰未戌";
                break;
            case "亥":
                full = "亥子";
                break;
            case "子":
                full = "亥子";
                break;
            case "寅":
                full = "寅卯";
                break;
            case "卯":
                full = "寅卯";
                break;
            case "巳":
                full = "巳午";
                break;
            case "午":
                full = "巳午";
                break;
            case "申":
                full = "申酉";
                break;
            case "酉":
                full = "申酉";
                break;
            default:
                break;
        }
        for (int i = 0; i < 6; i++) {
            if (full.contains(fushen_list.get(i).getText())) {
                if (liuqin_list.get(i).getText().toString().equals("子孙") || liuqin_list.get(i).getText().toString().equals("妻财")) {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(1, 2));
                } else {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(0, 1));
                }
            }
        }

        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_day_2);
        temp = (TextView) findViewById(R.id.liuqin_day);
        part = textView.getText().toString();
        full = new String();
        switch (part) {
            case "丑":
                full = "丑辰未戌";
                break;
            case "辰":
                full = "丑辰未戌";
                break;
            case "未":
                full = "丑辰未戌";
                break;
            case "戌":
                full = "丑辰未戌";
                break;
            case "亥":
                full = "亥子";
                break;
            case "子":
                full = "亥子";
                break;
            case "寅":
                full = "寅卯";
                break;
            case "卯":
                full = "寅卯";
                break;
            case "巳":
                full = "巳午";
                break;
            case "午":
                full = "巳午";
                break;
            case "申":
                full = "申酉";
                break;
            case "酉":
                full = "申酉";
                break;
            default:
                break;
        }

        for (int i = 0; i < 6; i++) {
            if (full.contains(fushen_list.get(i).getText())) {
                if (liuqin_list.get(i).getText().toString().equals("子孙") || liuqin_list.get(i).getText().toString().equals("妻财")) {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(1, 2));
                } else {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(0, 1));
                }
            }
        }

        textView = (TextView) findViewById(R.id.liuyaoresult_ganzhi_hour_2);
        temp = (TextView) findViewById(R.id.liuqin_hour);
        part = textView.getText().toString();
        full = new String();
        switch (part) {
            case "丑":
                full = "丑辰未戌";
                break;
            case "辰":
                full = "丑辰未戌";
                break;
            case "未":
                full = "丑辰未戌";
                break;
            case "戌":
                full = "丑辰未戌";
                break;
            case "亥":
                full = "亥子";
                break;
            case "子":
                full = "亥子";
                break;
            case "寅":
                full = "寅卯";
                break;
            case "卯":
                full = "寅卯";
                break;
            case "巳":
                full = "巳午";
                break;
            case "午":
                full = "巳午";
                break;
            case "申":
                full = "申酉";
                break;
            case "酉":
                full = "申酉";
                break;
            default:
                break;
        }
        for (int i = 0; i < 6; i++) {
            if (full.contains(fushen_list.get(i).getText())) {
                if (liuqin_list.get(i).getText().toString().equals("子孙") || liuqin_list.get(i).getText().toString().equals("妻财")) {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(1, 2));
                } else {
                    temp.setText(liuqin_list.get(i).getText().toString().substring(0, 1));
                }
            }
        }

    }

    //填上神表
    //前端实现，高能预警（在第二个版本的时候，这段逻辑被封存，真是谢天谢地。如果需要填神表，请根据文档理解逻辑再尝试实现）
    private void printShenTable() {
//        String temp = LiuYaoNewRecord.yongshen_selected;
//        if (temp.equals("父母"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//            if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//            if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//            if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//            if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//            if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//        }
//        if (temp.equals("兄弟"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//            if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//            if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//            if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//            if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//            if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//        }
//        if (temp.equals("官鬼"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//            if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//            if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//            if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//            if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//            if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//        }
//        if (temp.equals("子孙"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//            if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//            if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//            if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//            if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//            if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//        }
//        if (temp.equals("妻财"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//            if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//            if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//            if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//            if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//            textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//            if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//        }
//
//        //如果选择世应的填入，核能预警
//        if (temp.equals("世"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_shiying_1);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_1);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_2);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_2);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_3);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_3);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_4);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_4);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_5);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_5);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_6);
//            if(textView.getText().toString().equals("世")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_6);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//        }
//
//        if (temp.equals("应"))
//        {
//            TextView textView=(TextView)findViewById(R.id.liuyaoresult_shiying_1);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_1);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_2);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_2);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_3);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_3);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_4);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_4);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_5);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_5);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//
//            textView=(TextView)findViewById(R.id.liuyaoresult_shiying_6);
//            if(textView.getText().toString().equals("应")){
//                textView=(TextView)findViewById(R.id.liuyaoresult_liuqin_1_6);
//                temp=textView.getText().toString();
//                if (temp.equals("父母"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("父")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("兄弟"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("兄")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("官鬼"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("官")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("子孙"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("孙")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//                if (temp.equals("妻财"))
//                {
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_1);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_1);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_2);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_2);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_3);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_3);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_4);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_4);ys_textView.setText("用");}
//                    textView=(TextView)findViewById(R.id.liuyaoresult_qintable_5);
//                    if(textView.getText().toString().equals("财")){TextView ys_textView=(TextView)findViewById(R.id.liuyaoresult_shentable_5);ys_textView.setText("用");}
//                }
//            }
//        }
    }


}
