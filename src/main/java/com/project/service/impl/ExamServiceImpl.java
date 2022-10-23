package com.project.service.impl;

import com.google.common.collect.Lists;
import com.project.dto.AnswerDto;
import com.project.dto.ExamDto;
import com.project.dto.MediaDto;
import com.project.dto.QuestionDto;
import com.project.entity.AnswerEntity;
import com.project.entity.ExamEntity;
import com.project.entity.MediaEntity;
import com.project.entity.QuestionEntity;
import com.project.enums.QuestionType;
import com.project.repository.ExamRepository;
import com.project.repository.ExamRepositoryCustom;
import com.project.request.ExamFilterRequest;
import com.project.request.ExamRequest;
import com.project.response.ExamResponse;
import com.project.service.AnswerService;
import com.project.service.ExamService;
import com.project.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository repository;

    @Autowired
    private ExamRepositoryCustom repositoryCustom;

    private QuestionService questionService;

    @Autowired
    public ExamServiceImpl(@Lazy QuestionService questionService) {
        this.questionService = questionService;
    }

    @Autowired
    private AnswerService answerService;

    @Autowired
    private ModelMapper mapper;

    private CellStyle createBorderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);

        return style;
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.index);
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);

        return style;
    }

    private CellStyle createCenterTextStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        return style;
    }

    static String[] HEADER_QUESTION = {
            "STT",
            "Nội dung",
            "Cấp độ",
            "Thang điểm",
            "Loại câu hỏi",
            "Lĩnh vực",
            "Câu trả lời",
            "Đáp án",
            "Media"
    };
    private final static List<Function<QuestionEntity, Object>> questionExtractors = Arrays.asList(
            QuestionEntity::getContent,
            QuestionEntity::getLevel,
            QuestionEntity::getMaxPoint,
            q -> q.getType().name(),
            QuestionEntity::getCategory
    );

    private static final int ANSWER_COLUMN_NUMBER = 6;
    private static final int IS_RESULT_COLUMN_NUMBER = 7;
    private static final int MEDIA_COLUMN_NUMBER = 8;

    @Override
    public ExamResponse findAll() {
        List<ExamEntity> entities = repository.findByDeletedFalse();

        ExamResponse response = new ExamResponse();
        if (entities.isEmpty()) {
            response.setMessage("exam not found");
            response.setStatusCode(HttpStatus.OK);
        } else {
            List<ExamDto> dtos = new ArrayList<>();
            entities.forEach(e -> dtos.add(toDto(e)));

            response.setDtos(dtos);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    @Override
    public ExamResponse findOne(Long id) {
        ExamEntity exam = null;
        try {
            exam = repositoryCustom.findExamById(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        ExamResponse response = new ExamResponse();
        if (exam == null) {
            response.setMessage("exam not found");
            response.setStatusCode(HttpStatus.OK);
        } else {
            ExamDto dto = examToExamDto(exam);

            response.setDto(dto);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }


        return response;
    }

    private ExamDto examToExamDto(ExamEntity exam) {
        ExamDto examDto = mapper.map(exam, ExamDto.class);
        List<QuestionEntity> questions = exam.getQuestions();
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (QuestionEntity question : questions) {
            QuestionDto questionDto = mapper.map(question, QuestionDto.class);
            List<MediaEntity> images = question.getImages();
            List<MediaDto> imageDtos = images.stream()
                    .map(img -> mapper.map(img, MediaDto.class))
                    .collect(Collectors.toList());
            List<AnswerEntity> answers = question.getAnswers();
            List<AnswerDto> answerDtos = answers.stream()
                    .map(ans -> mapper.map(ans, AnswerDto.class))
                    .collect(Collectors.toList());
            questionDto.setImages(imageDtos);
            questionDto.setAnswers(answerDtos);
            questionDto.setLevel(question.getLevel());
            questionDtos.add(questionDto);
        }
        examDto.setQuestions(questionDtos);

        return examDto;
    }

    @Override
    public ExamResponse create(ExamRequest req) {
        ExamResponse response = new ExamResponse();
        try {
            ExamEntity entity = repository.save(mapper.map(req, ExamEntity.class));
            ExamDto dto = mapper.map(entity, ExamDto.class);

            response.setDto(dto);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("ERROR");
            response.setStatusCode(HttpStatus.BAD_REQUEST);
        }
        return response;
    }


    @Override
    public ExamResponse update(ExamRequest req) {

        ExamResponse response = new ExamResponse();
        if (!repository.existsById(req.getId())) {
            response.setMessage("Not found exam");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            ExamEntity entity = repository.save(mapper.map(req, ExamEntity.class));
            ExamDto dto = mapper.map(entity, ExamDto.class);

            response.setDto(dto);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    @Override
    public ExamResponse delete(Long id) {

        Optional<ExamEntity> optional = repository.findById(id);

        ExamResponse response = new ExamResponse();
        if (!optional.isPresent()) {
            response.setMessage("Answer is not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            ExamEntity entity = optional.get();
            entity.setDeleted(true);
            ExamDto dto = mapper.map(repository.save(entity), ExamDto.class);

            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    public ExamEntity findById(Long id) {

        ExamEntity exam = repositoryCustom.findExamById(id);
        if (exam != null) {
            return exam;
        }

        return null;
    }

    @Override
    public ExamResponse findPublicExam(Long id) {
		/*Optional<ExamEntity> examEntityOptional = repository.findById(id);
		if(!examEntityOptional.isPresent()){
			return new ResponseEntity<>("exam not found",HttpStatus.NOT_FOUND);
		}

		ExamEntity exam = examEntityOptional.get();

		List<QuestionEntity> questions = questionService.findByExam(exam);
		List<AnswerEntity> answers = answerService.findByQuestionIn(questions);

		// 2
		Map<Long, List<AnswerEntity>> mapAnswersByQuestionId = answers.stream()
				.collect(Collectors.groupingBy(a -> a.getQuestion().getId()));

		//3
		//*Map<Long, List<AnswerEntity>> map = new HashMap<>();
		for (AnswerEntity answer : answers) {
			Long questionId = answer.getQuestion().getId();
			if (map.containsKey(questionId)) {
				map.get(questionId).add(answer);
			}

			List<AnswerEntity> ans = new ArrayList<>();
			ans.add(answer);

			map.put(questionId, ans);
		}*/

		/*
		for (QuestionEntity question : questions) {
			question.setAnswers(mapAnswersByQuestionId.get(question.getId()));
		}

		exam.setQuestions(questions);*/
        ExamEntity exam = null;

        try {
            exam = repositoryCustom.findExamByIdAndCodeNull(id);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        ExamResponse response = new ExamResponse();
        if (exam == null) {
            response.setMessage("exam not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            ExamDto examDto = examToExamDto(exam);

            examDto.setQuestions(randomQuestion(examDto.getQuestions()));

            response.setDto(examDto);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }

        return response;
    }

    private List<QuestionDto> randomQuestion(List<QuestionDto> questions) {
        questions.forEach(q -> {
            if(q.getLevel() == null) {
                q.setLevel(1);
            }
        });

        List<QuestionDto> quesLv1 = getQuestionByLevel(questions, 1);
        List<QuestionDto> quesLv2 = getQuestionByLevel(questions, 2);
        List<QuestionDto> quesLv3 = getQuestionByLevel(questions, 3);
        List<QuestionDto> quesLv4 = getQuestionByLevel(questions, 4);

        Collections.shuffle(quesLv1);
        Collections.shuffle(quesLv2);
        Collections.shuffle(quesLv3);
        Collections.shuffle(quesLv4);

        List<QuestionDto> result = new ArrayList<>();
        result.addAll(quesLv1);
        result.addAll(quesLv2);
        result.addAll(quesLv3);
        result.addAll(quesLv4);

        return result;
    }

    private List<QuestionDto> getQuestionByLevel(List<QuestionDto> questions, int level) {
        return questions.stream().filter(q -> level == q.getLevel()).collect(Collectors.toList());
    }

    @Override
    public ExamResponse findExamsByParamNative(ExamFilterRequest request) {
        String code = request.getCode();
        if (code != null)
            code = "%" + request.getCode() + "%";

        String title = request.getTitle();
        if (title != null)
            title = "%" + request.getTitle() + "%";

        List<ExamEntity> entities = repository.findExamsByParamNative(code, title,
                request.getStart(), request.getEnd());

        ExamResponse response = new ExamResponse();
        if (entities.isEmpty()) {
            response.setMessage("Exams not found");
            response.setStatusCode(HttpStatus.NOT_FOUND);
        } else {
            List<ExamDto> dtos = new ArrayList<>();
            entities.forEach(e -> dtos.add(mapper.map(e, ExamDto.class)));
            response.setDtos(dtos);
            response.setMessage("OK");
            response.setStatusCode(HttpStatus.OK);
        }
        return response;
    }

    @Override
    public ExamResponse findPublicExams() {
        List<ExamEntity> entities = repository.findByCodeNullAndDeletedFalse();

        List<ExamDto> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(toDto(e)));
        ExamResponse response = new ExamResponse();
        response.setDtos(dtos);
        response.setMessage("Ok");
        response.setStatusCode(HttpStatus.OK);

        return response;
    }

    public ByteArrayInputStream genExamExcelFile(Long examId) {
        ExamEntity exam = repositoryCustom.findExamById(examId);

        if (exam == null) {
            return null;
        }

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Question");
            CellStyle headerCellStyle = createHeaderCellStyle(workbook);
            CellStyle borderCellStyle = createBorderCellStyle(workbook);
            printHeaders(sheet, headerCellStyle, HEADER_QUESTION, 0);

            int rowIndex = 1;
            List<QuestionEntity> questions = exam.getQuestions();
            int questionIndex = 0;
            for (QuestionEntity question : questions) {
                int imagesSize = question.getImages() == null ? 0 : question.getImages().size();
                int ansSize = question.getAnswers() == null ? 0 : question.getAnswers().size();
                int rowQuantity = Math.max(imagesSize, ansSize);
                if(rowQuantity == 0) {
                    rowQuantity++;
                }
                printQuestion(question, borderCellStyle, questionIndex, creatRows(sheet, rowIndex, rowQuantity));
                rowIndex += rowQuantity;
                questionIndex++;
            }

            adjustWidthCollumn(sheet);

            workbook.write(out);

            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException ioException) {
            throw new RuntimeException("fail to gen questions excel file: " + ioException.getMessage());
        }
    }

    private List<Row> creatRows(Sheet sheet, int indexStart, int quantity) {
        List<Row> result = new ArrayList<>();
        for (int i = indexStart; i < indexStart + quantity; i++) {
            result.add(sheet.createRow(i));
        }

        return result;
    }

    private void adjustWidthCollumn(Sheet sheet) {
        // STT
        sheet.setColumnWidth(0, 1000);
        // noi dung
        sheet.setColumnWidth(1, 15000);
        // cap do
        sheet.setColumnWidth(2, 2500);
        // thang diem
        sheet.setColumnWidth(3, 2500);
        // loai cau hoi
        sheet.setColumnWidth(4, 2500);
        // linh vuc
        sheet.setColumnWidth(5, 5000);
        // cau tra loi
        sheet.setColumnWidth(6, 10000);
        // dap an
        sheet.setColumnWidth(7, 2500);
        // media
        sheet.setColumnWidth(8, 10000);
    }

    public void printHeaders(Sheet sheet, CellStyle style, String[] headers, int headerRowNumber) {
        Row headerRow = sheet.createRow(headerRowNumber);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    public void printQuestion(QuestionEntity question, CellStyle cellStyle, int questionIndex, List<Row> rows) {
        if (question == null) {
            return;
        }

        Row firstRow = rows.get(0);
        int columnIndex = 0;
        creatAndPrintCell(firstRow.createCell(columnIndex++), cellStyle, q -> questionIndex, question);
        for (Function<QuestionEntity, Object> questionExtractor : questionExtractors) {
            creatAndPrintCell(firstRow.createCell(columnIndex++), cellStyle, questionExtractor, question);
        }

        List<AnswerEntity> answers = question.getAnswers();
        int ansIndex = 0;
        for (AnswerEntity answer : answers) {
            creatAndPrintCell(rows.get(ansIndex).createCell(ANSWER_COLUMN_NUMBER), cellStyle, AnswerEntity::getContent, answer);
            creatAndPrintCell(rows.get(ansIndex).createCell(IS_RESULT_COLUMN_NUMBER), cellStyle, AnswerEntity::getIsResult, answer);
            ansIndex++;
        }

        List<MediaEntity> medias = question.getImages();
        int mediaIndex = 0;
        for (MediaEntity media : medias) {
            creatAndPrintCell(rows.get(mediaIndex).createCell(MEDIA_COLUMN_NUMBER), cellStyle, MediaEntity::getPath, media);
            mediaIndex++;
        }
    }

    public <T, R> void creatAndPrintCell(Cell cell, CellStyle cellStyle, Function<T, R> extractor, T t) {
        R r = extractor.apply(t);
        if (r == null) {
            cell.setCellValue(" ");
        }

        if (r instanceof Double) {
            cell.setCellValue((double) r);
            return;
        }

        if (r instanceof Long) {
            cell.setCellValue((long) r);
            return;
        }

        if (r instanceof Integer) {
            cell.setCellValue((int) r);
            return;
        }

        if (r instanceof String) {
            cell.setCellValue((String) r);
            return;
        }

        if (r instanceof Date) {
            cell.setCellValue((Date) r);
        }
    }

    ExamDto toDto(ExamEntity entity) {
        ExamDto dto = new ExamDto();

        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setStartFrom(entity.getStartFrom());
        dto.setEndTo(entity.getEndTo());

        return dto;
    }

    public void createExamFromExcelFile(Long examId,MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        List<Row> rows = Lists.newArrayList(sheet.rowIterator());
        rows.remove(0);
        List<QuestionEntity> questions = new ArrayList<>();

        List<Row> rs = new ArrayList<>();
        for (Row row : rows) {
            Cell firstCell = row.cellIterator().next();
            if (firstCell.getColumnIndex() > 0) {
                rs.add(row);
            } else {
                if(rs.size() == 0) {
                    rs.add(row);
                    continue;
                }
                questions.add(createQuestionFromRows(rs));
                rs = new ArrayList<>();
                rs.add(row);
            }
        }

        ExamEntity exam = repository.findByIdAndDeletedFalse(examId);
        //set exam for questions
        questions.forEach(q-> q.setExam(exam));
        questionService.saveAll(questions);
    }

    @Override
    public ExamEntity findAllInfoExamById(Long examId) {
        return repositoryCustom.findExamById(examId);
    }

    private QuestionEntity createQuestionFromRows(List<Row> rows) {
        QuestionEntity question = new QuestionEntity();
        Row firstRow = rows.get(0);
        List<Cell> cells = Lists.newArrayList(firstRow.cellIterator());
        setContentForQuestion(question, cells.get(1));
        setMaxPointForQuestion(question, cells.get(3));
        setTypeForQuestion(question, cells.get(4));

        List<AnswerEntity> answers = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            answers.add(createAnswerFromRow(rows.get(i), i));
        }

        question.setAnswers(answers);

        //todo create media

        return question;
    }

    private void setContentForQuestion(QuestionEntity question, Cell cell) {
        try {
            question.setContent(cell.getStringCellValue());
        } catch (Exception exception) {
            log.info("set content for question fail");
        }
    }

    private void setMaxPointForQuestion(QuestionEntity question, Cell cell) {
        try {
            question.setMaxPoint(Double.valueOf(cell.getNumericCellValue()).intValue());
        } catch (Exception exception) {
            log.info("set max point for question fail");
        }
    }

    private void setTypeForQuestion(QuestionEntity question, Cell cell) {
        try {
            question.setType(QuestionType.valueOf(cell.getStringCellValue()));
        } catch (Exception exception) {
            log.info("set type for question fail");
        }
    }

    private AnswerEntity createAnswerFromRow(Row row, int index) {

        AnswerEntity answer = new AnswerEntity();
        List<Cell> cells = Lists.newArrayList(row.cellIterator());
        if (index == 0) {
            setContentForAnswer(answer, cells.get(6));
            setResultForAnswer(answer, cells.get(7));
        } else {
            setContentForAnswer(answer, cells.get(0));
            setResultForAnswer(answer, cells.get(1));
        }

        return answer;
    }

    private void setContentForAnswer(AnswerEntity answer, Cell cell) {
        try {
            answer.setContent(cell.getStringCellValue());
        } catch (Exception e) {
            log.info("set content for answer fail");
        }
    }

    private void setResultForAnswer(AnswerEntity answer, Cell cell) {
        try {
            answer.setIsResult(Double.valueOf(cell.getNumericCellValue()).intValue());
        } catch (Exception e) {
            log.info("set content for answer fail");
        }
    }
}
