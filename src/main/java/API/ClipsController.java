package API;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

import API.Model.Clip;
import com.sun.xml.internal.xsom.impl.util.Uri;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clips")
public class ClipsController {

    private ClipRepository clipRepository;
    private TwitchLookupService twitchLookupService;

    public ClipsController(ClipRepository clipRepository, TwitchLookupService twitchLookupService) {
        this.clipRepository = clipRepository;
        this.twitchLookupService = twitchLookupService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Clip> getAll() {
        List<Clip> clips = clipRepository.findAll();
        return clips;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Clip getClidById(@PathVariable("id") String slug) {
        Clip clip = clipRepository.findOne(slug);
        return clip;
    }


    //TODO This might be slow for a REST call (waiting for the Twitch API to respond)
    //TODO might have to improve the submit system
    //Not the usual POST route but in this case this fits better
    @PostMapping("/{slug}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Clip> submitClipBy(@PathVariable("slug") String slug)   {
        CompletableFuture<Clip> clip = null;
        clip = twitchLookupService.getClip(slug);
        //Wait till the clip is available
        CompletableFuture.allOf(clip).join();
        if (clip != null) {
            try {
                clipRepository.save(clip.get());
                return new ResponseEntity<>(clip.get(), null,HttpStatus.CREATED);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                return new ResponseEntity<>(null, null,HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (ExecutionException ee) {
                ee.printStackTrace();
                return new ResponseEntity<>(null, null,HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(null, null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}