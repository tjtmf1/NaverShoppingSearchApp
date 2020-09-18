package com.example.hackdayshoppingsearch.ui.viewholder;

import android.view.View;
import android.widget.TextView;

import com.example.hackdayshoppingsearch.R;
import com.example.hackdayshoppingsearch.container.LoginUser;
import com.example.hackdayshoppingsearch.container.UserProfile;
import com.example.hackdayshoppingsearch.ui.viewholder.BaseViewHolder;

import java.util.Locale;

import androidx.annotation.NonNull;

public class PopularTrendHeaderViewHolder extends BaseViewHolder<Void> {
    private TextView contentTextView;
    public PopularTrendHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        contentTextView = itemView.findViewById(R.id.popular_trend_list_header_content);
    }

    @Override
    public void bindData(Void v) {
        String ageString , genderString;
        UserProfile userProfile = LoginUser.getInstance().getUserProfile();
        if(userProfile != null) {
            ageString = getAgeString(userProfile.getAge());
            genderString = getGenderString(userProfile.getGender());
            contentTextView.setText(String.format(Locale.getDefault(), "%s %s에게 인기있는 카테고리입니다.", ageString, genderString));
        } else {
            contentTextView.setText("최근 인기있는 카테고리입니다.");
        }
    }

    private String getGenderString(String gender) {
        if (gender.equals("M")) {
            return "남성";
        } else if (gender.equals("F")) {
            return "여성";
        } else {
            return "";
        }
    }

    private String getAgeString(String age) {
        String ageString = "";
        if(age != null) {
            ageString = age.substring(0, 2) + "대";
        }
        return ageString;
    }
}
