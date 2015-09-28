package com.bluechilli.racingreminders.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by monishi on 26/06/15.
 */
public class Entity extends SugarRecord<Entity> {

    public enum FollowType {
        AapHorse
    }

    public int externalId;
    public String name;
    public String summary;
    public String description;
    public boolean following;
    public FollowType followType;
    @Ignore
    public String apiUserKey;
}
