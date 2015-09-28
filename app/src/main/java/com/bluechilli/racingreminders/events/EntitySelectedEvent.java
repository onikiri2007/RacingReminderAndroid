package com.bluechilli.racingreminders.events;

import com.bluechilli.racingreminders.models.Entity;

import java.util.Collection;

/**
 * Created by monishi on 3/07/15.
 */
public class EntitySelectedEvent {
    private final Collection<Entity> entities;

    public EntitySelectedEvent(Collection<Entity> entities) {
        this.entities = entities;
    }

    public Collection<Entity> getEntities() {
        return entities;
    }
}
