package com.example.marvinjason.art;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static String extension ="";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomDialog customDialog;
    public static boolean status = false;
    private boolean isSelectedSale = false;
    private boolean isSelectedNew = false;
    private boolean isSelectedPopular = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tl_activityMain);
        viewPager = (ViewPager) findViewById(R.id.vp_activityMain);
        customDialog = new CustomDialog();

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position)
                {
                    case 0:
                        return new SaleFragment();
                    case 1:
//                        return new NewFragment();
                        return new SaleFragment();
                    case 2:
//                        return new PopularFragment();
                        return new SaleFragment();
                    case 3:
                        if (status) {
                            return new AccountUserFragment();
                        }
                        else
                            return new AccountGuestFragment();
                    default:
                        return new SaleFragment();
                }
            }

            @Override
            public int getCount() {
                return 4;
            }

        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        setTitle("Items");
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_sale_active);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_new_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_popular_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.icon_account_inactive);
                        isSelectedSale = true;
                        isSelectedNew = false;
                        isSelectedPopular = false;
                        break;
                    case 1:
                        setTitle("New");
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_sale_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_new_active);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_popular_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.icon_account_inactive);
                        isSelectedSale = false;
                        isSelectedNew = true;
                        isSelectedPopular = false;
                        break;
                    case 2:
                        setTitle("Popular");
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_sale_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_new_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_popular_active);
                        tabLayout.getTabAt(3).setIcon(R.drawable.icon_account_inactive);
                        isSelectedSale = false;
                        isSelectedNew = false;
                        isSelectedPopular = true;
                        break;
                    default:
                        setTitle("Account");
                        tabLayout.getTabAt(0).setIcon(R.drawable.icon_sale_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.icon_new_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.icon_popular_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.icon_account_active);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_sale_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_new_inactive);
        tabLayout.getTabAt(2).setIcon(R.drawable.icon_popular_inactive);
        tabLayout.getTabAt(3).setIcon(R.drawable.icon_account_inactive);

        setTitle("Items");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (status) {
            getMenuInflater().inflate(R.menu.menu, menu);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSubmitButtonEnabled(true);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_LONG).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //startActivity(new Intent(MainActivity.this, SearchActivity.class));
                    Toast.makeText(MainActivity.this, newText, Toast.LENGTH_LONG).show();
                    return true;
                }
            });
        }
        else {
            getMenuInflater().inflate(R.menu.unloggedmenu, menu);
        }
//        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.ic_menu_sort_alphabetically);
//        drawable.setColorFilter(new PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY));
//        menu.findItem(R.id.action_sort).setIcon(drawable);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_category:
                CharSequence colors[] = new CharSequence[] {"Processor", "Memory", "Motherboard", "Graphics Card", "Networking", "Power Supply", "Storage", "All Items"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Categories:");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=processor";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=processor";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=processor";
                                }
                                break;
                            case 1:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=memory";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=memory";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=memory";
                                }
                                break;
                            case 2:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=motherboard";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=motherboard";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=motherboard";
                                }
                                break;
                            case 3:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=graphics-card";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=graphics-card";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=graphics-card";
                                }
                                break;
                            case 4:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=networking";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=networking";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=networking";
                                }
                                break;
                            case 5:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=power-supply";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=power-supply";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=power-supply";
                                }
                                break;
                            case 6:
                                if(isSelectedSale){
                                    SaleFragment.extension = "?category=storage";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "&category=storage";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "&category=storage";
                                }
                                break;
                            case 7:
                                if(isSelectedSale){
                                    SaleFragment.extension = "";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "";
                                }
                                break;
                            default:
                                if(isSelectedSale){
                                    SaleFragment.extension = "";
                                } else if (isSelectedNew) {
                                    NewFragment.extension = "";
                                } else if (isSelectedPopular) {
                                    PopularFragment.extension = "";
                                }
                                break;
                        }
                    }
                });
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
