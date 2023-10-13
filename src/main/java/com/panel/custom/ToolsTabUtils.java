package com.panel.custom;





import org.yifan.hao.DateUtil;
import org.yifan.hao.StringUtil;
import org.yifan.hao.WinUtils;

import java.util.ArrayList;
import java.util.List;

public class ToolsTabUtils {
    public static void m1() {
        StringBuilder sb = new StringBuilder();
        List<String> strs = new ArrayList<>();
        try {
            StringUtil.readStrByLins(WinUtils.getSysClipboardText(), lin -> {
                lin = lin.trim();
                if (lin.contains("=")) {
                    String s = lin.split("=")[0].split(" ")[1];
                    strs.add(s);
                    if (sb.length() > 0) {
                        sb.append(" + \" , ");
                    } else {
                        sb.append("\"");
                    }
                    sb.append(s).append(" = \"").append(" + ").append(s);
//                            System.out.println("s = " + s);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        String oneLog = sb.toString();
        if (oneLog.startsWith("\" ,")) {
            oneLog = sb.substring(1);
        }
        if (oneLog.endsWith("+")) {
            oneLog = sb.substring(0, oneLog.length() - 1);
        }
        oneLog = "Log.i(\"调试信息\", \"" + DateUtil.formatDate("yyyyMMddkkmmssSSS") + ": \"  + " + oneLog + ");";
        System.out.println(oneLog);
        StringBuilder twoLog = new StringBuilder();
        for (String str : strs) {
            if (twoLog.length() > 0) {
                twoLog.append(",");
            } else {
                twoLog.append("XLogUtil.printArgs(\"调试信息\" , ");
            }
            twoLog.append(str);
        }
        System.out.println(twoLog.append(");"));
        WinUtils.setSysClipboardText(oneLog + "\n" + twoLog);
    }


    public static void m2() {
        //这个例子中，我们为LinearLayout定义了两种状态：按下状态（state_pressed="true"）和普通状态。按下状态时，背景颜色为灰色（#D3D3D3），普通状态时，背景颜色为白色（#FFFFFF）。同时，我们为背景添加了圆角（radius="5dp"）。
        String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <item android:state_pressed=\"true\">\n" +
                "        <shape>\n" +
                "            <solid android:color=\"#D3D3D3\"/>\n" +
                "            <corners android:radius=\"5dp\"/>\n" +
                "        </shape>\n" +
                "    </item>\n" +
                "    <item>\n" +
                "        <shape>\n" +
                "            <solid android:color=\"#FFFFFF\"/>\n" +
                "            <corners android:radius=\"5dp\"/>\n" +
                "        </shape>\n" +
                "    </item>\n" +
                "</selector>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m3() {
        String tmp = "\n//设置背景\n\n" +
                "android:background=\"@drawable/tmp_bg\"\n\n" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <solid android:color=\"#FFFFFF\"/>\n" +
                "    <corners android:radius=\"4dp\"/>\n" +
                "    <stroke android:color=\"#CCCCCC\" android:width=\"0.5dp\"/>\n" +
                "</shape>\n\n" +
                "<!--1.shape: 形状，取值可以是rectangle（矩形）、oval（椭圆）、line（线）、ring（环形）。\n" +
                "        2.corners: 圆角，可以设置四个角的半径，也可以单独设置某一个角的半径。\n" +
                "        3.solid: 填充色，可以设置shape的填充颜色。\n" +
                "        4.stroke: 描边，可以设置shape的描边颜色和宽度。\n" +
                "        5.size: 大小，可以设置shape的宽度和高度。\n" +
                "        6.gradient: 渐变，可以设置shape的渐变颜色、渐变类型（linear、radial、sweep）、渐变角度等。\n" +
                "        7.padding: 内边距，可以设置shape的上下左右的内边距。" +
                "          8.渐变:linear：线性渐变，从起点到终点进行颜色过渡。\n" +
                "          9.radial：放射性渐变，从中心点向四周进行颜色过渡。\n" +
                "          10.sweep：扫描线渐变，以中心点为中心，进行360度的颜色过渡。-->";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m4() {
        String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <item android:color=\"@color/color_pressed\" android:state_pressed=\"true\" />\n" +
                "    <item android:color=\"@color/color_normal\" />\n" +
                "</selector>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m5() {
        String tmp = "     /**\n" +
                "     * 调用处子类方法所在位置\n" +
                "     *\n" +
                "     * @param msg smg\n" +
                "     */\n" +
                "    public static String getChildLog(String msg) {\n" +
                "        String content = msg;\n" +
                "        if (isConsoleLog) {\n" +
                "            StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];\n" +
                "            String stackTraceMsgArr = stackTraceElement.toString();\n" +
                "            content = stackTraceMsgArr.substring(stackTraceMsgArr.indexOf(\"(\")) + \"#\" + stackTraceElement.getMethodName() + \" msg:\" + msg;\n" +
                "            Log.i(TAG, content);\n" +
                "        }\n" +
                "        return content;\n" +
                "    }";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m6() {
        String tmp = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<shape xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:shape=\"oval\">\n" +
                "    <solid android:color=\"#40000000\"/>\n" +
                "    <size android:width=\"71dp\"\n" +
                "        android:height=\"71dp\"/>\n" +
                "</shape>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m7() {
        String tmp = "\n" +
                "        app:layout_constraintTop_toTopOf=\"parent\"\n" +
                "        app:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                "        app:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                "        app:layout_constraintRight_toRightOf=\"parent\"\n" +
                "        \n" +
                "        app:layout_constraintTop_toTopOf=\"@id/tmp\"\n" +
                "        app:layout_constraintTop_toBottomOf=\"@id/tmp\"\n" +
                "        app:layout_constraintBottom_toTopOf=\"@id/tmp\"\n" +
                "        app:layout_constraintBottom_toBottomOf=\"@id/tmp\"\n" +
                "        app:layout_constraintLeft_toLeftOf=\"@id/tmp\"\n" +
                "        app:layout_constraintLeft_toRightOf=\"@id/tmp\"\n" +
                "        app:layout_constraintRight_toRightOf=\"@id/tmp\"\n" +
                "        app:layout_constraintRight_toLeftOf=\"@id/tmp\"";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW0() {
        String tmp = " <androidx.appcompat.widget.AppCompatImageView\n" +
                "            android:layout_width=\"wrap_content\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            app:srcCompat=\"@drawable/ic_voice_record_bg\"\n" +
                "\n" +
                "            app:layout_constraintTop_toTopOf=\"parent\"\n" +
                "\t\t\tapp:layout_constraintBottom_toBottomOf=\"parent\"\n" +
                "\t\t\tapp:layout_constraintLeft_toLeftOf=\"parent\"\n" +
                "\t\t\tapp:layout_constraintRight_toRightOf=\"parent\"\n" +
                "\t\t\t\n" +
                "\t\t\tapp:layout_constraintTop_toTopOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintTop_toBottomOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintBottom_toTopOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintBottom_toBottomOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintLeft_toLeftOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintLeft_toRightOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintRight_toRightOf=\"@id/tmp\"\n" +
                "\t\t\tapp:layout_constraintRight_toLeftOf=\"@id/tmp\"\n" +
                "            />";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m9() {
        String tmp = "android:button=\"@drawable/custom_checkbox\"\n\n" +
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                " <selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <item android:drawable=\"@drawable/ic_checkbox_checked\" android:state_checked=\"true\" />\n" +
                "    <item android:drawable=\"@drawable/ic_checkbox_unchecked\" />\n" +
                "</selector>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW4() {
        String tmp = "\n" +
                "    <androidx.swiperefreshlayout.widget.SwipeRefreshLayout\n" +
                "        android:id=\"@+id/swipe_refresh_layout\"\n" +
                "        android:layout_width=\"match_parent\"\n" +
                "        android:layout_height=\"match_parent\">\n" +
                "\n" +
                "        <androidx.recyclerview.widget.RecyclerView\n" +
                "            android:id=\"@+id/refresh\"\n" +
                "            android:layout_width=\"match_parent\"\n\n" +
                "            tools:itemCount=\"10\"\n" +
                "            tools:listitem=\"@layout/item_rv\"" +
                "            android:layout_height=\"match_parent\" />\n" +
                "    </androidx.swiperefreshlayout.widget.SwipeRefreshLayout>\n" +
                "\n" +
                " ArrayList<TestUtils.DataTestUser> dataUsers = TestUtils.getDataUsers(15);\n" +
                "        mAdapter = new LoadMoreAdapter<>(R.layout.list_item, new LoadMoreAdapter.BindViewByData<TestUtils.DataTestUser>() {\n" +
                "            @Override\n" +
                "            public void bindView(LoadMoreAdapter.BaseHolder holder, View itemView, TestUtils.DataTestUser data, int postion) {\n" +
                "                holder.setText(android.R.id.text1, data.getUserName());\n" +
                "            }\n" +
                "        });\n" +
                "        mAdapter.setOnLoadMoreListener(new LoadMoreAdapter.OnLoadMoreListener() {\n" +
                "            @Override\n" +
                "            public void onLoadMore() {\n" +
                "                new Handler().postDelayed(new Runnable() {\n" +
                "                    @Override\n" +
                "                    public void run() {\n" +
                "                        mAdapter.addDatas(TestUtils.getDataUsers(3), 2);\n" +
                "                    }\n" +
                "                }, 300);\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        RecyclerView refresh = findViewById(R.id.refresh);\n" +
                "        refresh.setLayoutManager(new LinearLayoutManager(this));\n" +
                "        refresh.setAdapter(mAdapter);\n" +
                "        mAdapter.addSwipeRefreshToRecyclerView(refresh, new SwipeRefreshLayout.OnRefreshListener() {\n" +
                "            @Override\n" +
                "            public void onRefresh() {\n" +
                "                mAdapter.setDatas(TestUtils.getDataUsers(13), 22);\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        mAdapter.setDatas(dataUsers, 40);\n" +
                "\t\t\n" +
                "\t\t\n" +
                "        SwipeRefreshLayout swipe_refresh_layout;\n" +
                "        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);\n" +
                "        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {\n" +
                "            @Override\n" +
                "            public void onRefresh() {\n" +
                "                swipe_refresh_layout.setRefreshing(false);\n" +
                "            }\n" +
                "        });\n" +
                "\t\t\n" +
                "\t\t\n" +
                "\t\t<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<TextView xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "    android:id=\"@android:id/text1\"\n" +
                "    android:layout_width=\"match_parent\"\n" +
                "    android:layout_height=\"wrap_content\"\n" +
                "    android:background=\"?android:attr/selectableItemBackground\"\n" +
                "    android:gravity=\"center\"\n" +
                "    android:minHeight=\"?android:attr/listPreferredItemHeight\"\n" +
                "    android:textAppearance=\"?android:attr/textAppearanceListItem\" />\n" +
                "\t\t";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m11() {

        String tmp = "<selector xmlns:android=\"http://schemas.android.com/apk/res/android\">\n" +
                "    <item android:drawable=\"@drawable/your_pressed_background\"\n" +
                "          android:state_pressed=\"true\" />\n" +
                "    <item android:drawable=\"@drawable/your_default_background\" />\n" +
                "</selector>\n\n" +
                "    android:background=\"@drawable/shadow_effect\"\n";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW1() {

        String tmp = "<androidx.appcompat.widget.AppCompatTextView\n" +
                "                android:id=\"@+id/item_call_tv_del\"\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tandroid:layout_marginLeft=\"10dp\"\n" +
                "                android:layout_marginRight=\"10dp\"\n" +
                "                android:layout_marginTop=\"10dp\"\n" +
                "                android:layout_marginBottom=\"10dp\"\n" +
                "                android:paddingRight=\"10dp\"\n" +
                "                android:paddingBottom=\"10dp\"\n" +
                "                android:paddingLeft=\"10dp\"\n" +
                "                android:paddingTop=\"10dp\"\n" +
                "                android:background=\"@color/black\"\n" +
                "                android:layout_marginLeft=\"10dp\"\n" +
                "\t\t\t\t\n" +
                "                android:textColor=\"@color/black\"\n" +
                "                android:gravity=\"center\"\n" +
                "                android:text=\"删除\"\n" +
                "\t\t\t\ttools:text=\"群名称\"\n" +
                "                android:textColor=\"@color/white\"\n" +
                "                android:textSize=\"16sp\" />";
        WinUtils.setSysClipboardText(tmp);

    }

    public static void m13() {

        String tmp = "\n" +
                "maven { url 'https://maven.aliyun.com/repository/jcenter' } // 阿里云镜像\n" +
                "maven { url 'https://maven.aliyun.com/repository/google' } // 阿里云镜像\n\n" +
                "maven { url 'https://jitpack.io' }\n\n" +
                "classpath 'com.android.tools.build:gradle:3.5.3'\n" +
                "distributionUrl=https\\://services.gradle.org/distributions/gradle-5.4.1-all.zip" +
                "\n\n" +
                "    // ButterKnife\n" +
                "    implementation 'com.jakewharton:butterknife:10.2.3'\n" +
                "    annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.3'\n" +
                "    implementation 'com.blankj:utilcode:1.30.7'\n" +
                "    implementation 'com.tencent:mmkv:1.2.16'\n" +
                "    implementation 'com.github.foyuexiaoerbuyu:Mutils:0.35'\n" +
                "     // 权限请求框架：https://github.com/getActivity/XXPermissions\n" +
                "    implementation 'com.github.getActivity:XXPermissions:18.3'" +
                "    implementation 'com.google.code.gson:gson:2.10.1'";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW3() {
        String tmp = "<LinearLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"horizontal\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:id=\"@+id/webrtc_tv_\"\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:gravity=\"center\"\n" +
                "                android:text=\"测试\"\n" +
                "                android:textColor=\"@color/black\" />\n" +
                "\n" +
                "            <EditText\n" +
                "                android:id=\"@+id/webrtc_edt_ws_add\"\n" +
                "                android:layout_width=\"0dp\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:layout_weight=\"1\"\n" +
                "                android:hint=\"ws地址\" />\n" +
                "\n" +
                "            <Button\n" +
                "                android:id=\"@+id/webrtc_btn_save\"\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"保存\" />\n" +
                "        </LinearLayout>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW2() {
        String tmp = "\n" +
                "            <androidx.appcompat.widget.AppCompatTextView\n" +
                "                android:id=\"@+id/zfb_tv_title\"\n" +
                "                android:layout_width=\"0dp\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:layout_weight=\"1\"\n" +
                "                android:gravity=\"left\"\n" +
                "                android:padding=\"10dp\"\n" +
                "                android:src=\"@drawable/icon_zfb_top_l_arr\"\n" +
                "                android:text=\"全部待还\"\n" +
                "                android:textColor=\"@color/black\"\n" +
                "                android:textSize=\"18sp\" />";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW5() {
        String tmp = "String[] items3 = new String[]{\"按钮0\", \"按钮1\", \"按钮2\", \"按钮3\", \"按钮4\", \"按钮5\", \"按钮6\"};//创建item\n" +
                "        //添加列表\n" +
                "        new AlertDialog.Builder(this)\n" +
                "                .setItems(items3, (dialogInterface, pos) -> {\n" +
                "                    Toast.makeText(MainActivity.this, \"点的是：\" + items3[pos], Toast.LENGTH_SHORT).show();\n" +
                "                    if (pos == 0) {\n" +
                "                    } else if (pos == 1) {\n" +
                "                    } else if (pos == 2) {\n" +
                "                    } else if (pos == 3) {\n" +
                "                    } else if (pos == 4) {\n" +
                "                    } else if (pos == 5) {\n" +
                "                    } else {\n" +
                "                    }\n" +
                "                }).create().show();";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW6() {
        String tmp = "AlertDialog alertDialog4 = new AlertDialog.Builder(this)\n" +
                "        .setTitle(\"选择您喜欢的老湿\")\n" +
                "        .setIcon(R.mipmap.ic_launcher)\n" +
                "        .setSingleChoiceItems(items4, 0, new DialogInterface.OnClickListener() {//添加单选框\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i) {\n" +
                "                index = i;\n" +
                "            }\n" +
                "        })\n" +
                "        .setPositiveButton(\"确定\", new DialogInterface.OnClickListener() {//添加\"Yes\"按钮\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i) {\n" +
                "                Toast.makeText(AlertDialogActivity.this, \"这是确定按钮\" + \"点的是：\" + items4[index], Toast.LENGTH_SHORT).show();\n" +
                "            }\n" +
                "        })\n" +
                " \n" +
                "        .setNegativeButton(\"取消\", new DialogInterface.OnClickListener() {//添加取消\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i) {\n" +
                "                Toast.makeText(AlertDialogActivity.this, \"这是取消按钮\", Toast.LENGTH_SHORT).show();\n" +
                "            }\n" +
                "        })\n" +
                "        .create();\n" +
                "alertDialog4.show();";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void mW7() {
        String tmp = "final String[] items5 = new String[]{\"苍老湿\", \"小泽老湿\", \"波多野结衣老湿\", \"吉泽明步老湿\"};//创建item\n" +
                "final boolean[] booleans = {true, true, false, false};\n" +
                "AlertDialog alertDialog5 = new AlertDialog.Builder(this)\n" +
                "        .setTitle(\"选择您喜欢的老湿\")\n" +
                "        .setIcon(R.mipmap.ic_launcher)\n" +
                "        .setMultiChoiceItems(items5, booleans, new DialogInterface.OnMultiChoiceClickListener() {//创建多选框\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i, boolean b) {\n" +
                "                booleans[i] = b;\n" +
                "            }\n" +
                "        })\n" +
                "        .setPositiveButton(\"确定\", new DialogInterface.OnClickListener() {//添加\"Yes\"按钮\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i) {\n" +
                "                StringBuilder stringBuilder = new StringBuilder();\n" +
                "                for (int a = 0; a < booleans.length; a++) {\n" +
                "                    if (booleans[a]) {\n" +
                "                        stringBuilder.append(items5[a] + \" \");\n" +
                "                    }\n" +
                "                }\n" +
                "                Toast.makeText(AlertDialogActivity.this, \"这是确定按钮\" + \"点的是：\" + stringBuilder.toString(), Toast.LENGTH_SHORT).show();\n" +
                "            }\n" +
                "        })\n" +
                " \n" +
                "        .setNegativeButton(\"取消\", new DialogInterface.OnClickListener() {//添加取消\n" +
                "            @Override\n" +
                "            public void onClick(DialogInterface dialogInterface, int i) {\n" +
                "                Toast.makeText(AlertDialogActivity.this, \"这是取消按钮\", Toast.LENGTH_SHORT).show();\n" +
                "            }\n" +
                "        })\n" +
                "        .create();\n" +
                "alertDialog5.show();";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void m12() {
        String tmp = "\n" +
                "    //谷歌流布局\n" +
                "    implementation 'com.google.android.flexbox:flexbox:3.0.0'\n" +
                "\t\n" +
                "\t <com.google.android.flexbox.FlexboxLayout\n" +
                "                android:id=\"@+id/flexboxLayout\"\n" +
                "                android:layout_width=\"match_parent\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                app:flexWrap=\"wrap\">\n" +
                "\t</com.google.android.flexbox.FlexboxLayout>";
        WinUtils.setSysClipboardText(tmp);
    }

    public static void tmp() {
        String tmp = "";
        WinUtils.setSysClipboardText(tmp);
    }
}
