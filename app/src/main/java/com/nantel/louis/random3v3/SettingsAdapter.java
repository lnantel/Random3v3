package com.nantel.louis.random3v3;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Louis Nantel on 2017-05-06.
 *
 */

public class SettingsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<SettingsCategory> data;
    private LayoutInflater inflater;
    private Typeface typeface;

    public SettingsAdapter(Context context, ArrayList<SettingsCategory> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/big_noodle_titling_oblique.ttf");
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).settings.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).settings.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.settings_category_item, null);
        }

        SettingsCategory sc = (SettingsCategory) getGroup(groupPosition);


        Button settingAll = (Button) convertView.findViewById(R.id.settingAll);
        Button settingNone = (Button) convertView.findViewById(R.id.settingNone);
        TextView settingCategoryTxt = (TextView) convertView.findViewById(R.id.settingCategoryTxt);
        settingCategoryTxt.setTypeface(typeface);
        settingCategoryTxt.setText(sc.name);
        if(isExpanded && sc.name.equals("Heroes")){
            settingAll.setVisibility(View.VISIBLE);
            settingNone.setVisibility(View.VISIBLE);
        }else{
            settingAll.setVisibility(View.GONE);
            settingNone.setVisibility(View.GONE);
        }

        settingAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setAllHeroes(true);
                notifyDataSetChanged();
            }
        });

        settingNone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setAllHeroes(false);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.settings_item, null);
        }

        String s = (String) getChild(groupPosition,childPosition);

        Button settingOn = (Button) convertView.findViewById(R.id.settingOn);
        Button settingOff = (Button) convertView.findViewById(R.id.settingOff);
        Button deleteBtn = (Button) convertView.findViewById(R.id.deleteButton);
        deleteBtn.setTypeface(typeface);
        TextView settingNameTxt = (TextView) convertView.findViewById(R.id.settingNameTxt);
        settingNameTxt.setTypeface(typeface);
        settingNameTxt.setText(s);

        deleteBtn.setTag(R.id.categoryName, ((SettingsCategory)getGroup(groupPosition)).name);
        deleteBtn.setTag(R.id.itemName, s);
        settingOn.setTag(R.id.categoryName, ((SettingsCategory)getGroup(groupPosition)).name);
        settingOn.setTag(R.id.itemName, s);
        settingOff.setTag(R.id.categoryName, ((SettingsCategory)getGroup(groupPosition)).name);
        settingOff.setTag(R.id.itemName, s);

        if(((SettingsCategory)getGroup(groupPosition)).name.equals("Team comp")){
            deleteBtn.setVisibility(View.GONE);
            boolean status = false;
            switch(s){
                case "At least one healer":
                    status = ((MainActivity)context).oneHealer;
                    break;
                case "At least one tank":
                    status = ((MainActivity)context).oneTank;
                    break;
                case "At least one DPS":
                    status = ((MainActivity)context).oneDPS;
                    break;
            }
            if(status){
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }else{
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }
        }

        if(((SettingsCategory)getGroup(groupPosition)).name.equals("Heroes")){
            deleteBtn.setVisibility(View.GONE);
            boolean heroOn = false;
            MainActivity act = (MainActivity)context;
            for(Hero hero : act.heroesOn){
                if(hero.name.equals(s)) heroOn = true;
            }

            if(heroOn){
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }else{
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }
        }

        if(((SettingsCategory)getGroup(groupPosition)).name.equals("Roles")){
            deleteBtn.setVisibility(View.GONE);
            boolean status = ((MainActivity)context).getRoleStatus(s);
            if(status){
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }else{
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }
        }

        if(((SettingsCategory)getGroup(groupPosition)).name.equals("Presets")){
            deleteBtn.setVisibility(View.VISIBLE);
            boolean status = ((MainActivity)context).getPresetStatus(s);
            if(status){
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }else{
                settingOff.setBackgroundColor(context.getResources().getColor(R.color.colorLightBlue));
                settingOn.setBackgroundColor(context.getResources().getColor(R.color.colorBlackTransparent));
            }
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getTag(R.id.categoryName).equals("Presets"))
                    ((MainActivity)context).deletePreset((String)v.getTag(R.id.itemName));
                notifyDataSetChanged();
            }
        });

        settingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.settingOn){
                    if(v.getTag(R.id.categoryName).equals("Heroes"))
                        ((MainActivity)context).setHero((String)v.getTag(R.id.itemName),true);
                    if(v.getTag(R.id.categoryName).equals("Roles"))
                        ((MainActivity)context).setRole((String)v.getTag(R.id.itemName),true);
                    if(v.getTag(R.id.categoryName).equals("Presets"))
                        ((MainActivity)context).setPreset((String)v.getTag(R.id.itemName),true);
                    if(v.getTag(R.id.categoryName).equals("Team comp")){
                        switch((String)v.getTag(R.id.itemName)){
                            case "At least one healer":
                                ((MainActivity)context).oneHealer = true;
                                break;
                            case "At least one tank":
                                ((MainActivity)context).oneTank = true;
                                break;
                            case "At least one DPS":
                                ((MainActivity)context).oneDPS = true;
                                break;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        settingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.settingOff){
                    if(v.getTag(R.id.categoryName).equals("Heroes"))
                        ((MainActivity)context).setHero((String)v.getTag(R.id.itemName),false);
                    if(v.getTag(R.id.categoryName).equals("Roles"))
                        ((MainActivity)context).setRole((String)v.getTag(R.id.itemName),false);
                    if(v.getTag(R.id.categoryName).equals("Presets"))
                        ((MainActivity)context).setPreset((String)v.getTag(R.id.itemName),false);
                    if(v.getTag(R.id.categoryName).equals("Team comp")){
                        switch((String)v.getTag(R.id.itemName)){
                            case "At least one healer":
                                ((MainActivity)context).oneHealer = false;
                                break;
                            case "At least one tank":
                                ((MainActivity)context).oneTank = false;
                                break;
                            case "At least one DPS":
                                ((MainActivity)context).oneDPS = false;
                                break;
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
