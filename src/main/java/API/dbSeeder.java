package API;

import API.Model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//Component
public class dbSeeder implements CommandLineRunner {
    private ClipRepository clipRepository;

    public dbSeeder(ClipRepository clipRepository) {
        this.clipRepository = clipRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Clip clip = new Clip("PopularGleamingHerbsWTRuck", "182283255",
                "https://clips.twitch.tv/PopularGleamingHerbsWTRuck?tt_medium=clips_api&tt_content=url",
                "https://clips.twitch.tv/embed?clip=PopularGleamingHerbsWTRuck&tt_medium=clips_api&tt_content=embed",
                "<iframe src='https://clips.twitch.tv/embed?clip=PopularGleamingHerbsWTRuck&tt_medium=clips_api&tt_content=embed'" +
                        " width='640' height='360' frameborder='0' scrolling='no' allowfullscreen='true'></iframe>",
                new Broadcaster("23161357", "lirik", "LIRIK", "https://www.twitch.tv/lirik",
                        "https://static-cdn.jtvnw.net/jtv_user_pictures/lirik-profile_image-476e7a592cdfed74-150x150.png"),
                new Curator("185864807", "urnowthinkingmanually","urnowthinkingmanually",
                        "https://www.twitch.tv/urnowthinkingmanually",
                        "https://static-cdn.jtvnw.net/user-default-pictures/49988c7b-57bc-4dee-bd4f-6df4ad215d3a-profile_image-150x150.jpg"),
                new Vod("221902624", "https://www.twitch.tv/videos/221902624?t=1h44m18s", 6258,
                        "https://static-cdn.jtvnw.net/s3_vods/122d2312b1201412c2ea_lirik_27378388096_779819425/thumb/thumb0-320x240.jpg"),
                "27378388096","Sea of Thieves","en", "Boring streamer btw", 35968, 55,
                "2018-01-24T19:10:19Z",
                new Thumbnails("https://clips-media-assets.twitch.tv/182283255-preview-480x272.jpg",
                        "https://clips-media-assets.twitch.tv/182283255-preview-260x147.jpg",
                "https://clips-media-assets.twitch.tv/182283255-preview-86x45.jpg")
                );

        this.clipRepository.deleteAll();
        this.clipRepository.save(clip);
    }
}