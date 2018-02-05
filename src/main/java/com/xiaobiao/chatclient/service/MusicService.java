package com.xiaobiao.chatclient.service;

import com.xiaobiao.chatclient.mapper.MusicMapper;
import com.xiaobiao.chatclient.model.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wuxiaobiao
 * @create 2017-10-09 9:58
 * To change this template use File | Settings | Editor | File and Code Templates.
 **/
@Component
public class MusicService {

    @Autowired
    private MusicMapper musicMapper;

    @Transactional(readOnly = false, isolation = Isolation.DEFAULT,
            propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<Music> queryMusic(Music music) {
        List<Music> musicList = null;
        try {
            musicList = musicMapper.queryMusic(music);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return musicList;
    }

    @Transactional(readOnly = false, isolation = Isolation.DEFAULT,
            propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Integer insertMusic(Music music) {
        Integer result = null;
        try {
            result = musicMapper.insertMusic(music);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
