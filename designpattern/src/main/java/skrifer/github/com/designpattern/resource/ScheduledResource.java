package skrifer.github.com.designpattern.resource;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ScheduledResource extends Resource {

    public ScheduledResource(String name) {
        super(name);
    }

    public ScheduledResource(String name, int historyCapacity) {
        super(name, historyCapacity);
    }

    public ScheduledResource(String name, String resourceType) {
        super(name, resourceType);
    }

    public ScheduledResource(String name, String resourceType, int historyCapacity) {
        super(name, resourceType, historyCapacity);
    }




}
