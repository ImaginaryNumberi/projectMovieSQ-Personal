package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.TheaterEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TheaterMapper {

    TheaterEntity[] selectTheaters();

    TheaterEntity[] selectRegionTheater();

    String selectImageByTheaterName(String theaterName);

}