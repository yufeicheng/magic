package com.magic.interview.service.leaf;

import com.sankuai.inf.leaf.common.Result;
import com.sankuai.inf.leaf.service.SegmentService;
import com.sankuai.inf.leaf.service.SnowflakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Cheng Yufei
 * @create 2021-01-19 3:39 下午
 **/
@Service
@Slf4j
public class LeafService {

    @Autowired
    private SegmentService segmentService;
    @Autowired
    private SnowflakeService snowflakeService;

    public Long generatorSegment(String type, String key) {
        Result result = new Result();
        switch (type) {
            case "segment":
                result = segmentService.getId(key);
                break;
            case "snowflake":
                result = snowflakeService.getId(key);
                break;
            default:
                break;
        }
        return result.getId();
    }
}
