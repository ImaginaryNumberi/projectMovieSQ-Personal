package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.ScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ScheduleMapper {

    ScheduleEntity getSchedule(int index);

    void insertSchedule(ScheduleEntity schedule);

    int deleteSchedule(@Param(value = "index") int index);

    int updateSchedule(ScheduleEntity movieSchedule);
}
