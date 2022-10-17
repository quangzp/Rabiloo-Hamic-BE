package com.project.service.impl;

import com.project.repository.MediaRepository;
import com.project.request.MediaRequest;
import com.project.response.MediaResponse;
import com.project.service.MediaService;
import com.project.service.QuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl implements MediaService {

	@Autowired
	private MediaRepository repository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private ModelMapper mapper;


	@Override
	public MediaResponse findAll() {
		/*List<MediaEntity> entities = repository.findAll();
		if(entities.isEmpty()){
			return new ResponseEntity<>("there image/video is null", HttpStatus.NOT_FOUND);
		}
		List<MediaDto> dtos = new ArrayList<>();
		entities.forEach(e -> dtos.add(mapper.map(e, MediaDto.class)));

		return new ResponseEntity<>(dtos,HttpStatus.OK);*/

		return null;
	}

	@Override
	public MediaResponse findOne(Long id) {

		/*Optional<MediaEntity> mediaEntityOptional = repository.findById(id);
		if(!mediaEntityOptional.isPresent()){
			return new ResponseEntity<>("Not found",HttpStatus.NOT_FOUND);
		}

		MediaDto dto = mapper.map(mediaEntityOptional.get(),MediaDto.class);
		return new ResponseEntity<>(dto,HttpStatus.OK);*/

		return null;
	}

	@Override
	public MediaResponse create(MediaRequest req) {
		return save(req);
	}

	@Override
	public MediaResponse update(MediaRequest req) {
		/*Optional<MediaEntity> mediaEntityOptional = repository.findById(req.getId());
		if(!mediaEntityOptional.isPresent())
			return new ResponseEntity<>("media not found",HttpStatus.NOT_FOUND);

		return save(req);*/

		return null;
	}

	private MediaResponse save(MediaRequest req) {
		/*Optional<QuestionEntity> questionEntityOptional = questionService.findById(req.getQuestionId());
		if(!questionEntityOptional.isPresent()){
			return new ResponseEntity<>("Question not found",HttpStatus.BAD_REQUEST);
		}

		MediaEntity entity = mapper.map(req,MediaEntity.class);
		entity.setQuestion(questionEntityOptional.get());

		MediaDto dto = mapper.map(repository.save(entity),MediaDto.class);

		return new ResponseEntity<>(dto,HttpStatus.OK);*/

		return null;
	}


	@Override
	public MediaResponse delete(Long id) {
		/*try {
			repository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}*/

		return null;
	}
	
}
