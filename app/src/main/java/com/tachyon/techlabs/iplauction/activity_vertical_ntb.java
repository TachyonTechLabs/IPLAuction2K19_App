package com.tachyon.techlabs.iplauction;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class activity_vertical_ntb extends AppCompatActivity {
    private List<String> playername=new  ArrayList<>();
    private List<Long> playervalue=new ArrayList<>();
    private List<Integer> points=new ArrayList<>();
    ProgressBar pg;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_ntb);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initUI();


    }

    private void initUI() {

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_vertical_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 8;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.activity_opponent, null, false);

                ImageView team_logo=view.findViewById(R.id.team_logo);
                pg=view.findViewById(R.id.pg);
                pg.setVisibility(View.VISIBLE);
                switch (position)
                {
                    case 0: team_logo.setImageDrawable(getResources().getDrawable(R.drawable.mumbai_indians_min));
                        pg.setVisibility(View.GONE);
                            break;
                    case 1:team_logo.setImageDrawable(getResources().getDrawable(R.drawable.rcb_min));
                        pg.setVisibility(View.GONE);
                        break;
                    case 2:team_logo.setImageDrawable(getResources().getDrawable(R.drawable.mumbai_indians_min));
                        pg.setVisibility(View.GONE);
                        break;
                    case 3:team_logo.setImageDrawable(getResources().getDrawable(R.drawable.deccan_charges_min));
                        pg.setVisibility(View.GONE);
                        break;
                    case 4:team_logo.setImageDrawable(getResources().getDrawable(R.drawable.delhi_daredevils_min));
                        pg.setVisibility(View.GONE);
                        break;
                    case 5:team_logo.setImageDrawable(getResources().getDrawable(R.drawable.kkr_min));
                        pg.setVisibility(View.GONE);
                        break;

                        default:pg.setVisibility(View.GONE);
                            break;
                }


                ListView listView = view.findViewById(R.id.opponents_listview);
                opponents_team_playerslist_adapter opponents_team_playerslist_adapter = new opponents_team_playerslist_adapter(getApplicationContext(),playername,playervalue,points);
                listView.setAdapter(opponents_team_playerslist_adapter);


                final TextView txtPage = (TextView) view.findViewById(R.id.txt_vp_item_page);
                txtPage.setText(String.format("Page #%d", position));

                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.vertical_ntb);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_vertical);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.mumbai_indians_min),
                        Color.parseColor(colors[8]))
                        .title("ic_first")
                        .selectedIcon(getResources().getDrawable(R.drawable.mumbai_indians_min))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.rcb),
                        Color.parseColor(colors[8]))
                        .selectedIcon(getResources().getDrawable(R.drawable.rcb_min))
                        .title("ic_second")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.kkr_min),
                        Color.parseColor(colors[8]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ipl_logo))
                        .title("ic_third")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.deccan_charges_min),
                        Color.parseColor(colors[3]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ipl_logo))
                        .title("ic_fourth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.delhi_daredevils_min),
                        Color.parseColor(colors[6]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ipl_logo))
                        .title("ic_fifth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.kings11_min),
                        Color.parseColor(colors[6]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ipl_logo))
                        .title("ic_sixth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.kkr_min),
                        Color.parseColor(colors[6]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ipl_logo))
                        .title("ic_seventh")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 4);
    }
}