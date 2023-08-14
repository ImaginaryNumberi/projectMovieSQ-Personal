package com.ixtx.projectmoviesq.services;

import com.ixtx.projectmoviesq.entities.TheaterEntity;
import com.ixtx.projectmoviesq.mappers.TheaterMapper;
import org.springframework.stereotype.Service;

@Service
public class TheaterService {
    private final TheaterMapper theaterMapper;

    public TheaterService(TheaterMapper theaterMapper) {
        this.theaterMapper = theaterMapper;
    }

    public TheaterEntity[] getTheaters() {
        return this.theaterMapper.selectTheaters();
    }

    public TheaterEntity[] getTheaterRegion() {
        return this.theaterMapper.selectRegionTheater();
    }

    public String getTheaterImage(String theaterName) {
        return theaterMapper.selectImageByTheaterName(theaterName);
    }
}
