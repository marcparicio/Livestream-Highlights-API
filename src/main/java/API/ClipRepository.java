package API;

import API.Model.Clip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClipRepository extends MongoRepository<Clip,String>{
}
