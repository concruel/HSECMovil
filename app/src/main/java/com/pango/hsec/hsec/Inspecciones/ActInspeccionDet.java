package com.pango.hsec.hsec.Inspecciones;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.pango.hsec.hsec.Observaciones.MyPageAdapter;
import com.pango.hsec.hsec.Observaciones.MyTabFactory;
import com.pango.hsec.hsec.R;

import java.util.ArrayList;
import java.util.List;

public class ActInspeccionDet extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{

    MyPageAdapter pageAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    ImageButton close;
    String codObs;
    String urlObs;
    int pos,proviene;
    HorizontalScrollView horizontalscroll;

/*
    public static String jsonInspeccion="";
    public static String jsonEquipo="";
    public static String jsonParticipante="";

    public static String jsonObsIns="";
    public static String jsonComentario="";

*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_inspeccion_det);

        close=findViewById(R.id.imageButton);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        //GlobalVariables loaddata = new GlobalVariables();
        //loaddata.LoadData();
        horizontalscroll=findViewById(R.id.horizontalscroll);
        Bundle datos = this.getIntent().getExtras();
        codObs=datos.getString("codObs");
        pos=datos.getInt("posTab");
        //urlObs=datos.getString("UrlObs");
        //GlobalVariables.CodObs=codObs;



        initialiseTabHost();

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(ActInspeccionDet.this);


    }

    public void close(View view){
        finish();
    }



    // Method to add a TabHost
    private static void AddTab(ActInspeccionDet activity, TabHost tabHost,
                               TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    // Manages the Tab changes, synchronizing it with Pages
    public void onTabChanged(String tag) {


        pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    // Manages the Page changes, synchronizing it with Tabs

    //numero de tab que quieres mostrar


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if(pos==3){
            //pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(3);
            horizontalscroll.smoothScrollTo(((int) (getResources().getDimensionPixelSize(R.dimen.tab_width) * (pos))), 0);

            pos=0;

        }else{
            pos = this.mViewPager.getCurrentItem();
            this.mTabHost.setCurrentTab(pos);
        }



        View tabView = mTabHost.getTabWidget().getChildAt(position);
        if (tabView != null)
        {
            final int width = horizontalscroll.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            horizontalscroll.scrollTo(scrollPos, 0);
        } else {
            horizontalscroll.scrollBy(positionOffsetPixels, 0);
        }


    }

    @Override
    public void onPageSelected(int arg0) {
    }

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();

/*
		fList.add(Fragment.instantiate(this, ObsFragment.class.getName()));
		fList.add(Fragment.instantiate(this, GaleriaFragment.class.getName()));
		fList.add(Fragment.instantiate(this, PlanFragment.class.getName()));
		fList.add(Fragment.instantiate(this, ComentarioFragment.class.getName()));
		*/




        // TODO Put here your Fragments
        FragmentInspeccion f1 = FragmentInspeccion.newInstance(codObs);
        FragmentParticipante f2 = FragmentParticipante.newInstance(codObs);
        FragmentObsInsp f3 = FragmentObsInsp.newInstance(codObs);
        //MySampleFragment f4 = MySampleFragment.newInstance("Sample Fragment 4");
        FragmentComentIns f4=FragmentComentIns.newInstance(codObs);

        //ObsFragment f4 = ObsFragment.newInstance("","");

        fList.add(f1);
        fList.add(f2);
        fList.add(f3);
        fList.add(f4);



        return fList;
    }

    // Tabs Creation
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab1").setIndicator("Inspección"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab2").setIndicator("Participantes"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab3").setIndicator("Observaciones"));
        ActInspeccionDet.AddTab(this, this.mTabHost,
                this.mTabHost.newTabSpec("Tab4").setIndicator("Comentarios"));
        mTabHost.setOnTabChangedListener(this);

    }

}
