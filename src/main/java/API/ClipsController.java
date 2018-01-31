package API;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import API.Model.Clip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clips")
public class ClipsController {

    private static final int pageSize = 10;

    private ClipRepository clipRepository;
    private TwitchLookupService twitchLookupService;


    public ClipsController(ClipRepository clipRepository, TwitchLookupService twitchLookupService) {
        this.clipRepository = clipRepository;
        this.twitchLookupService = twitchLookupService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Clip> getAll(@RequestParam("page") int page) {
        Pageable pageable = new PageRequest(page, pageSize);
        Page<Clip> clipsPage = clipRepository.findAll(pageable);
        return clipsPage.getContent();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Clip getClidById(@PathVariable("id") String slug) {
        Clip clip = clipRepository.findOne(slug);
        return clip;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Clip> submitClipBy(@RequestParam("slug") String slug)   {
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