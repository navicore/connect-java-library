package com.urbanairship.connect.client.model.responses;

import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.urbanairship.connect.client.model.EventType;
import com.urbanairship.connect.client.model.GsonUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.Instant;

public class EventAdapter implements JsonDeserializer<Event> {

    // TODO: Add First open and Uninstall events when they become available
    private static final ImmutableBiMap<EventType, Type> typeMap = ImmutableBiMap.<EventType, Type>builder()
            .put(EventType.CLOSE, CloseEvent.class)
            .put(EventType.CUSTOM, CustomEvent.class)
            .put(EventType.LOCATION, LocationEvent.class)
            .put(EventType.OPEN, OpenEvent.class)
            .put(EventType.SEND, SendEvent.class)
            .put(EventType.TAG_CHANGE, TagChange.class)
            .build();

    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject eventJson = json.getAsJsonObject();
        Event.Builder eventBuilder = Event.newBuilder();

        // Parse appKey
        JsonElement appKey = eventJson.get(Event.APP_KEY);
        if (appKey == null || StringUtils.isEmpty(appKey.getAsString())) {
            throw new JsonParseException("Unable to parse event with missing appKey");
        }
        eventBuilder.setAppKey(appKey.getAsString());

        // Parse Event ID
        JsonElement eventId = eventJson.get(Event.EVENT_ID_KEY);
        if (eventId == null || StringUtils.isEmpty(eventId.getAsString())) {
            throw new JsonParseException("Unable to parse event with missing identifier");
        }
        eventBuilder.setIdentifier(eventId.getAsString());


        // Parse offset
        JsonElement offset = eventJson.get(Event.OFFSET_KEY);
        if (offset == null) {
            throw new JsonParseException("Unable to parse event with missing offset");
        }
        eventBuilder.setOffset(offset.getAsLong());

        // Parse Event Type
        JsonElement type = eventJson.get(Event.TYPE_KEY);
        if (type == null || StringUtils.isEmpty(type.getAsString())) {
            throw new JsonParseException("Unable to parse event with missing type");
        }

        EventType eventType;
        try {
            eventType = EventType.valueOf(type.getAsString());
            if (!typeMap.containsKey(eventType)) {
                throw new JsonParseException(String.format("Unable to create event of type: %s", type.getAsString()));
            }
        } catch (IllegalArgumentException e) {
            throw new JsonParseException(String.format("Unable to create event of type: %s", type.getAsString()), e);
        }
        eventBuilder.setEventType(eventType);

        // Parse device info
        JsonElement deviceInfo = eventJson.get(Event.DEVICE_INFO_KEY);
        if (deviceInfo == null) {
            throw new JsonParseException("Unable to parse event with missing deviceInfo");
        }
        eventBuilder.setDeviceInfo(DeviceInfo.parseJSON(deviceInfo.toString()));


        // Parse event body
        JsonElement eventBodyJson = eventJson.get(Event.EVENT_BODY_KEY);
        EventBody eventBody;
        if (eventBodyJson == null) {
            throw new JsonParseException("Unable to parse event with missing event body");
        }
        eventBody = GsonUtil.getGson().fromJson(eventBodyJson.toString(), typeMap.get(eventType));
        eventBuilder.setEventBody(eventBody);

        JsonElement occurredJson = eventJson.get(Event.OCCURRED_KEY);
        if (occurredJson == null) {
            throw new JsonParseException("Unable to parse event with missing occurred field");
        }

        JsonElement processedJson = eventJson.get(Event.OCCURRED_KEY);
        if (processedJson == null) {
            throw new JsonParseException("Unable to parse event with missing processed field");
        }

        // validate occurance string
        Instant occurred = GsonUtil.getGson().fromJson(occurredJson, Instant.class);
        // validate processed string
        Instant processed = GsonUtil.getGson().fromJson(processedJson, Instant.class);

        eventBuilder.setOccurred(occurred);
        eventBuilder.setProcessed(processed);

        return eventBuilder.build();
    }
}
