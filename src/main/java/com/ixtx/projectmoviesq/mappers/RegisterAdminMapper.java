package com.ixtx.projectmoviesq.mappers;

import com.ixtx.projectmoviesq.entities.ScheduleEntity;
import com.ixtx.projectmoviesq.models.PagingModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RegisterAdminMapper {
    int insertMovieSchedule(ScheduleEntity movieSchedule);

    int selectCount(@Param(value = "searchCriterion") String searchCriterion,
                    @Param(value = "searchQuery") String searchQuery);

    ScheduleEntity[] selectByPage(@Param(value = "pagingModel") PagingModel pagingModel,
                                  @Param(value = "searchCriterion") String searchCriterion,
                                  @Param(value = "searchQuery") String searchQuery);

    int deleteByIndex(@Param(value = "index") int index);

    int updateByIndex(@Param(value = "index") int index,
                      @Param(value = "movieIndex") int movieIndex,
                      @Param(value = "theaterIndex") int theaterIndex);

    ScheduleEntity[] selectAll();
}
