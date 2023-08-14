package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.entities.ScheduleEntity;
import com.ixtx.projectmoviesq.mappers.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public ScheduleService(ScheduleMapper scheduleMapper, JdbcTemplate jdbcTemplate) {
        this.scheduleMapper = scheduleMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ScheduleEntity> rowMapper = (rs, rowNum) -> {
        ScheduleEntity schedule = new ScheduleEntity();
        schedule.setIndex(rs.getInt("index"));
        schedule.setMovieIndex(rs.getInt("movie_index"));
        schedule.setTheaterIndex(rs.getInt("theater_index"));
        schedule.setScreenIndex(rs.getInt("screen_index"));
        schedule.setTimeStart(rs.getTimestamp("time_start"));
        schedule.setTimeEnd(rs.getTimestamp("time_end"));
        return schedule;
    };

    public List<ScheduleEntity> getAllSchedules() {
        String query = "SELECT * FROM movie_sq.screen_schedules";
        return jdbcTemplate.query(query, rowMapper);
    }

    public void addSchedule(ScheduleEntity schedule) {
        if (schedule.getMovieIndex() > 0 && schedule.getTheaterIndex() > 0 && schedule.getScreenIndex() > 0) {
            scheduleMapper.insertSchedule(schedule);
        } else {
            throw new RuntimeException("각 인덱스를 숫자로 넣어주세요");
        }
    }

    public boolean deleteSchedule(int index) {
        return this.scheduleMapper.deleteSchedule(index) > 0;
    }

    public ScheduleEntity updateSchedule(ScheduleEntity scheduleEntity) {
        int update = scheduleMapper.updateSchedule(scheduleEntity);
        // 성공하면 객체 반환, 실패하면 null 반환
        if (update > 0) {
            return scheduleMapper.getSchedule(scheduleEntity.getIndex());
        } else {
            return null;
        }
    }
}

