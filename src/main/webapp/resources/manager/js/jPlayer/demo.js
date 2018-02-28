



$(function () {
    $.post("music/queryMusic",
        {admin: $("#userCode").val()},
        function (data, status) {
            $.each(data.result, function (index, obj) {
                var str = "<li class='list-group-item'>"
                    + "<span class='pull-left thumb-xs m-t-xs avatar m-l-xs m-r-sm'>"
                    + "<i class='icon-music-tone-alt text-info'></i>"
                    + "</span>"
                    + "<div class='clear'>"
                    + "<div style='cursor:pointer'>"
                    + "<a type='button' onclick= \"javascript:toplay('"+obj.musicName+"','"+obj.url+"')\" style='color: black'>" + obj.musicName
                    + "</a></div>"
                    + "<small class='text-muted'>" + obj.userCode + "</small>"
                    + "</div>"
                    + "</li>";
                $("#musicList").append(str);
            })
        }, 'json')
});


function toplay(title, url) {
    //让上一首歌播放失效,可以实现动态切换播放的音乐
    $("#jplayer_N").jPlayer("destroy");
    //播放歌曲
    var myPlaylist = new jPlayerPlaylist({
        jPlayer: "#jplayer_N",
        cssSelectorAncestor: "#jp_container_N"
    }, [
        {
            title: title,
            artist: "小彪",
            mp3: url,
            poster: "/resources/manager/images/m0.jpg"
        },
        {
            title: "霍比特人3五军之战",
            artist: "小彪",
            mp3: "/resources/uploadFileDir/Two Steps From Hell - Victory.mp3",
            poster: "/resources/manager/images/m0.jpg"
        }
        // {
        //   title:"Chucked Knuckles",
        //   artist:"3studios",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_chuckedknuckles.mp3",
        //   poster: "images/m0.jpg"
        // },
        // {
        //   title:"Cloudless Days",
        //   artist:"ADG3 Studios",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_cloudlessdays.mp3",
        //   poster: "images/m0.jpg"
        // },
        // {
        //   title:"Core Issues",
        //   artist:"Studios",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_coreissues.mp3",
        //   poster: "images/m0.jpg"
        // },
        // {
        //   title:"Cryptic Psyche",
        //   artist:"ADG3",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_crypticpsyche.mp3",
        //   poster: "images/m0.jpg"
        // },
        // {
        //   title:"Electro Freak",
        //   artist:"Studios",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_electrofreak.mp3",
        //   poster: "images/m0.jpg"
        // },
        // {
        //   title:"Freeform",
        //   artist:"ADG",
        //   mp3:"http://flatfull.com/themes/assets/musics/adg3com_freeform.mp3",
        //   poster: "images/m0.jpg"
        // }
    ], {
        playlistOptions: {
            enableRemoveControls: true,
            autoPlay: true
        },
        swfPath: "/resources/manager/js/jPlayer",
        supplied: "webmv, ogv, m4v, oga, mp3",
        smoothPlayBar: true,
        keyEnabled: true,
        audioFullScreen: false
    });


    $(document).on($.jPlayer.event.pause, myPlaylist.cssSelector.jPlayer, function () {
        $('.musicbar').removeClass('animate');
        $('.jp-play-me').removeClass('active');
        $('.jp-play-me').parent('li').removeClass('active');
    });

    $(document).on($.jPlayer.event.play, myPlaylist.cssSelector.jPlayer, function () {
        $('.musicbar').addClass('animate');
    });

    $(document).on('click', '.jp-play-me', function (e) {
        e && e.preventDefault();
        var $this = $(e.target);
        if (!$this.is('a')) $this = $this.closest('a');

        $('.jp-play-me').not($this).removeClass('active');
        $('.jp-play-me').parent('li').not($this.parent('li')).removeClass('active');

        $this.toggleClass('active');
        $this.parent('li').toggleClass('active');
        if (!$this.hasClass('active')) {
            myPlaylist.pause();
        } else {
            var i = Math.floor(Math.random() * (1 + 7 - 1));
            myPlaylist.play(i);
        }

    });


    // video

    $("#jplayer_1").jPlayer({
        ready: function () {
            $(this).jPlayer("setMedia", {
                title: "Big Buck Bunny",
                m4v: "http://flatfull.com/themes/assets/video/big_buck_bunny_trailer.m4v",
                ogv: "http://flatfull.com/themes/assets/video/big_buck_bunny_trailer.ogv",
                webmv: "http://flatfull.com/themes/assets/video/big_buck_bunny_trailer.webm",
                poster: "images/m41.jpg"
            });
        },
        swfPath: "js",
        supplied: "webmv, ogv, m4v",
        size: {
            width: "100%",
            height: "auto",
            cssClass: "jp-video-360p"
        },
        globalVolume: true,
        smoothPlayBar: true,
        keyEnabled: true
    });
}