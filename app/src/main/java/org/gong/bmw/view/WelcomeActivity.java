package org.gong.bmw.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.gong.bmw.R;
import org.jetbrains.annotations.Nullable;

/**
 * @author: create by 龚志辉
 * @version: v1.0
 * @description: org.gong.bmw.view
 * @date:2019-09-11
 */
public class WelcomeActivity extends BaseActivity {

    private View btnStart;

    @Override
    public int getLayoutR() {
        return R.layout.layout_welcome;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnStart = findViewById(R.id.btnStart);
        btnStart.setOnClickListener(v ->
                {
                    startActivity(new Intent(getContext(), PlayActivity.class));
                    finish();
                }
        );
    }
}
