package com.xiaobiao.chatclient.mapper;


import com.xiaobiao.chatclient.model.Music;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MusicMapper {

    List<Music> queryMusic(Music music);

    int insertMusic(Music music);
}
