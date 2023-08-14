package com.ixtx.projectmoviesq.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixtx.projectmoviesq.dtos.MovieDto;
import com.ixtx.projectmoviesq.entities.*;
import com.ixtx.projectmoviesq.mappers.AdminMapper;
import com.sun.jna.platform.win32.WinCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@EnableScheduling
public class AdminService {
    private final AdminMapper adminMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public MovieEntity[] getMovieList() {
        return this.adminMapper.selectMovies();
    }

    // 영화 정보 저장
    @Transactional
    public boolean putMovieInformation(HttpServletRequest request, MovieEntity movie,
                                       String category, MultipartFile... files) throws IOException {
        // Trailer 재생 시, Browser Page Error 방지
        if (movie.getTrailerUrl().contains("www.youtube.com")) {
            movie.setTrailerUrl(movie.getTrailerUrl().replace("www.youtube.com", "www.youtube-nocookie.com"));
        }
        movie.setCreatedAt(new Date());
        if (this.adminMapper.insertMovie(movie) < 1) {
            return false;
        }
        int inserted = 0;
        // 영화 첨부 파일(포스터) 저장
        for (MultipartFile multipartFile : files) {
            MovieImageEntity movieImage = new MovieImageEntity()
                    .setMovieIndex(movie.getIndex())
                    .setImageName(multipartFile.getOriginalFilename())
                    .setImageSize((int) multipartFile.getSize())
                    .setImageType(multipartFile.getContentType())
                    .setImageData(multipartFile.getBytes())
                    .setCategory(category);
            inserted += this.adminMapper.insertMovieImage(movieImage);
        }
        return inserted == files.length;
    }

    // 영화 이미지 저장
    @Transactional
    public boolean putMovieImage(HttpServletRequest request, int index, String category, MultipartFile file) throws IOException {
        MovieImageEntity movieImage = new MovieImageEntity()
                .setMovieIndex(index)
                .setImageName(file.getOriginalFilename())
                .setImageSize((int) file.getSize())
                .setImageType(file.getContentType())
                .setImageData(file.getBytes())
                .setCategory(category);
        return this.adminMapper.insertMovieImage(movieImage) > 0;
    }

    // 영화 정보 수정
    @Transactional
    public boolean updateMovieInformation(HttpServletRequest request, MovieEntity movie, String category, int movieImageIndex, MultipartFile... files) throws IOException {
        // Trailer 재생 시, Browser Page Error 방지
        if (movie.getTrailerUrl().contains("www.youtube.com")) {
            movie.setTrailerUrl(movie.getTrailerUrl().replace("www.youtube.com", "www.youtube-nocookie.com"));
        }
        // 수정된 날짜로 reset
        movie.setCreatedAt(new Date());
        // 이미지 외의 정보 update
        if (this.adminMapper.updateMovie(movie) < 1) {
            return false;
        }
        // 영화 정보 중 이미지가 수정된 경우, 이전 이미지의 deleted flag 에  true setting
        int inserted = 0;
        if (movieImageIndex != 0) {
            MovieImageEntity movieImage = new MovieImageEntity();
            movieImage = this.adminMapper.selectMovieImageByIndex(movieImageIndex);
            movieImage.setDeleted(true);
            this.adminMapper.updateMovieImage(movieImage);
        }
        // 영화 첨부 파일(포스터) 저장
        for (MultipartFile multipartFile : files) {
            MovieImageEntity movieImage = new MovieImageEntity();
            movieImage = new MovieImageEntity()
                    .setMovieIndex(movie.getIndex())
                    .setImageName(multipartFile.getOriginalFilename())
                    .setImageSize((int) multipartFile.getSize())
                    .setImageType(multipartFile.getContentType())
                    .setImageData(multipartFile.getBytes())
                    .setCategory(category);
            inserted += this.adminMapper.insertMovieImage(movieImage);
        }
        return inserted == files.length;
    }

    // 공지사항 저장
    public boolean putNotice(HttpServletRequest request, HttpSession session, NoticeEntity notice, String category, MultipartFile... files) throws IOException {
        // if (article == null || article.getTitle().equals("") || article.getContent() == "") {
        //     return false;
        // }
        notice.setView(0)
                .setCreatedAt(new Date())
                .setDeleted(false)
                .setClientIp(request.getRemoteAddr())
                .setWriterEmail(((UserEntity) session.getAttribute("user")).getEmail());

        int inserted = 0;
        if (category.equals("faq")) {
            System.out.println("faq");
            inserted = this.adminMapper.insertFaq(notice);
        } else {
            System.out.println("notice");
            inserted = this.adminMapper.insertNotice(notice);
        }
        if (inserted < 1) {
            return false;
        }

        /* inserted = 0;
        for (MultipartFile multipartFile : files) {
            AttachmentEntity attachment = new AttachmentEntity()
                    .setArticleIndex(notice.getIndex())
                    .setFileName(multipartFile.getOriginalFilename())
                    .setFileSize((int) multipartFile.getSize())
                    .setFileContentType(multipartFile.getContentType())
                    .setFileData(multipartFile.getBytes());
            inserted += this.articleMapper.insertAttachment(attachment);
        }*/
        return inserted == files.length;
    }

    public boolean deleteNoticeByIndex(int index) {
        return this.adminMapper.deleteNoticeByIndex(index) > 0;
    }

    // 상영중 및 예정작 영화 리스트 가져오기
    public List<MovieDto> getMovies() {
        MovieEntity[] movies = this.adminMapper.selectMoviesByStatus();
        List<MovieDto> movieDtoList = new ArrayList<>();
        for (MovieEntity movie : movies) {
            MovieDto movieDto = new MovieDto();
            movieDto.setIndex(movie.getIndex())
                    .setTitleKo(movie.getTitleKo());
            movieDtoList.add(movieDto);
        }
        return movieDtoList;
    }

    // 영화 뉴스 저장
    @Transactional
    public boolean putMovieNews(HttpServletRequest request, NewsEntity news, MultipartFile file) throws IOException {
        news.setImageName(file.getOriginalFilename())
                .setImageSize((int) file.getSize())
                .setImageType(file.getContentType())
                .setImageData(file.getBytes());
        return this.adminMapper.insertMovieNews(news) > 0;
    }

    // 광고 정보 저장
    public boolean putCommercial(String index, String commercialName, CommercialEntity commercial, MultipartFile multipartFile) throws IOException {
        // 영화 첨부 파일(포스터) 저장
        commercial
                .setMovieIndex(Integer.parseInt(index))
                .setCommercialName(commercialName)
                .setImageName(multipartFile.getOriginalFilename())
                .setImageData(multipartFile.getBytes())
                .setImageSize((int) multipartFile.getSize())
                .setCreatedAt(new Date())
                .setDeleted(false);
        commercial.setHidden(commercial.getStartedAt().compareTo(commercial.getCreatedAt()) >= 0);
        return this.adminMapper.insertCommercial(commercial) > 0;

    }


    // 모든 광고 정보 가져오기 (삭제되지 않은)
    public CommercialEntity[] getCommercialList() {
        return this.adminMapper.selectAllCommercial();
    }

    // 모든 광고 정보 가져오기 (삭제 혹은 숨기지 않는)
    public CommercialEntity[] getCommercials() {
        return this.adminMapper.selectCommercialNotDeleted();
    }

    // 광고 이미지 가져오기
    public CommercialEntity getCommercialImage(int index) {
        return this.adminMapper.selectCommercialImageByIndex(index);
    }

    // 해당 광고 정보 가져오기 (1개)
    public CommercialEntity getCommercialByIndex(int index) {
        return this.adminMapper.selectCommercialByIndex(index);
    }

    //  매일 0 시 자동 업데이트 (광고 시작일, 종료일에 따라)
    @Scheduled(cron = "0 0 0 * * *")
    public void updateCommercial() {
        System.out.println("업데이트~");
        CommercialEntity[] commercials = this.adminMapper.selectCommercialNotDeleted();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String todayStr = sdf.format(today);
        for (CommercialEntity commercial : commercials) {
            if (todayStr.compareTo(sdf.format(commercial.getStartedAt())) >= 0 || todayStr.compareTo(sdf.format(commercial.getFinishedAt())) <= 0) {
                commercial.setHidden(false);
                this.adminMapper.updateCommercialByIndex(commercial);
            }
            if (todayStr.compareTo(sdf.format(commercial.getFinishedAt())) > 0) {
                commercial.setHidden(true);
                this.adminMapper.updateCommercialByIndex(commercial);
            }
        }
        System.out.println("업데이트완료~");
    }


    public Map<Integer, String> changeSentence(String sentence) {
        String newSentence = sentence.replace("{", "").replace("}", "").replace("\"", "");
        String[] array = newSentence.split(",");
        Map<Integer, String> map = new HashMap<>();
        for (String option : array) {
            String[] split = option.split(":");
            int index = Integer.parseInt(split[0]);
            String value = split[1];
            map.put(index, value);
        }
        return map;
    }

    // 광고 화면 상태 수정 (보이기->숨기기, 숨기기->보이기)
    public boolean updateCommercial(String dataResult) throws ParseException {
        Map<Integer, String> changeData = changeSentence(dataResult);
        int sum = 0;
        for (Map.Entry<Integer, String> entry : changeData.entrySet()) {
            // 내리기 -> 보여주기
            if (entry.getValue().equals("true")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = new GregorianCalendar(Locale.KOREA);
                cal.add(Calendar.DATE, +1);
                String tomorrow = sdf.format(cal.getTime());
                Date date = sdf.parse(tomorrow);
                CommercialEntity commercial = this.adminMapper.selectCommercialImageByIndex(entry.getKey());
                commercial.setFinishedAt(date);
                commercial.setHidden(false);
                int result = this.adminMapper.updateCommercialByIndex(commercial);
                sum += result;
                // 보여주기 -> 내리기
            } else if (entry.getValue().equals("false")) {
                CommercialEntity commercial = this.adminMapper.selectCommercialImageByIndex(entry.getKey());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = new GregorianCalendar(Locale.KOREA);
                cal.add(Calendar.DATE, -1);
                String yesterday = sdf.format(cal.getTime());
                Date date = sdf.parse(yesterday);
                commercial.setFinishedAt(date);
                commercial.setHidden(true);
                int result = this.adminMapper.updateCommercialByIndex(commercial);
                sum += result;
            }
        }
        return changeData.size() == sum;
    }

    // 광고 정보 수정
    public boolean updateCommercialInformation(int index, CommercialEntity commercial, MultipartFile multipartFile) throws IOException {
        CommercialEntity existingCommercial = this.adminMapper.selectCommercialByIndex(index);
        existingCommercial.setMovieIndex(commercial.getMovieIndex())
                .setCommercialName(commercial.getCommercialName())
                .setStartedAt(commercial.getStartedAt())
                .setFinishedAt(commercial.getFinishedAt())
                .setCreatedAt(new Date())
                .setDeleted(false)
                .setHidden(existingCommercial.getStartedAt().compareTo(existingCommercial.getCreatedAt()) >= 0);
        if (multipartFile == null ||multipartFile.isEmpty()) {
            existingCommercial.setImageName(existingCommercial.getImageName())
                    .setImageData(existingCommercial.getImageData())
                    .setImageSize(existingCommercial.getImageSize());
        } else {
            existingCommercial.setImageName(multipartFile.getOriginalFilename())
                    .setImageData(multipartFile.getBytes())
                    .setImageSize((int) multipartFile.getSize());
        }
        return this.adminMapper.updateCommercialInformationByIndex(existingCommercial) > 0;
    }

    // 광고 정보 삭제 (목록에서만)
    public boolean deleteCommercial(int index) {
        CommercialEntity commercial = this.adminMapper.selectCommercialImageByIndex(index);
        commercial.setDeleted(true);
        return this.adminMapper.updateCommercialByIndex(commercial) > 0;
    }
}