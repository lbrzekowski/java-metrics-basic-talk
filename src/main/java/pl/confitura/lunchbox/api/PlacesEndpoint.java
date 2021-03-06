package pl.confitura.lunchbox.api;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.confitura.lunchbox.domain.LunchPlace;
import pl.confitura.lunchbox.domain.LunchPlacesService;
import pl.confitura.lunchbox.domain.Voter;

import java.util.List;

@RestController
@RequestMapping("/places")
public class PlacesEndpoint {

    private final Logger logger = LoggerFactory.getLogger(PlacesEndpoint.class);

    private final LunchPlacesService lunchPlacesService;

    private final Voter voter;

    private final MetricRegistry metricRegistry;

    @Autowired
    public PlacesEndpoint(LunchPlacesService lunchPlacesService, Voter voter, MetricRegistry metricRegistry) {
        this.lunchPlacesService = lunchPlacesService;
        this.voter = voter;
        this.metricRegistry = metricRegistry;
    }

    @RequestMapping(method = RequestMethod.GET)
    @Timed(absolute = true, name = "endpoint.list")
    public List<LunchPlace> list() {
        return lunchPlacesService.list();
    }

    @RequestMapping(path = "/{lunchPlaceName}/upvote", method = RequestMethod.POST)
    @Timed(absolute = true, name = "endpoint.upvote")
    public void upvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.UPVOTE));
    }

    @RequestMapping(path = "/{lunchPlaceName}/downvote", method = RequestMethod.POST)
    @Timed(absolute = true, name = "endpoint.downvote")
    public void downvote(@PathVariable String lunchPlaceName) {
        voter.castVote(new Voter.Vote(lunchPlaceName, 1, Voter.VoteType.DOWNVOTE));
    }
}
