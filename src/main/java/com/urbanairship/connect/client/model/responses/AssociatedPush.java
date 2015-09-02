package com.urbanairship.connect.client.model.responses;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.Optional;

public class AssociatedPush {

    @SerializedName("push_id")
    private String pushId;
    @SerializedName("group_id")
    private Optional<String> groupId;
    @SerializedName("variant_id")
    private final Optional<Integer> variantId;
    private final Optional<Instant> time;

    private AssociatedPush() {
        this(null, Optional.empty(), Optional.empty(), Optional.empty());
    }

    public AssociatedPush(String pushId, Optional<String> groupId, Optional<Integer> variantId, Optional<Instant> time) {
        this.pushId = pushId;
        this.groupId = groupId;
        this.variantId = variantId;
        this.time = time;
    }


    public String getPushId() {
        return pushId;
    }

    public Optional<String> getGroupId() {
        return groupId;
    }

    public Optional<Integer> getVariantId() {
        return variantId;
    }

    public Optional<Instant> getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AssociatedPush)) return false;

        AssociatedPush that = (AssociatedPush) o;

        if (!groupId.equals(that.groupId)) return false;
        if (!pushId.equals(that.pushId)) return false;
        if (!time.equals(that.time)) return false;
        if (!variantId.equals(that.variantId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pushId.hashCode();
        result = 31 * result + groupId.hashCode();
        result = 31 * result + variantId.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }


}