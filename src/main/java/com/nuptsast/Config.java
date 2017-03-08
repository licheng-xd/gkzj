package com.nuptsast;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lc on 17/3/8.
 */
@Configuration
public class Config {

    @Value("${image.path}")
    private String imagePath;

    @Value("${exam.once.count}")
    private int onceCount;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getOnceCount() {
        return onceCount;
    }

    public void setOnceCount(int onceCount) {
        this.onceCount = onceCount;
    }
}
