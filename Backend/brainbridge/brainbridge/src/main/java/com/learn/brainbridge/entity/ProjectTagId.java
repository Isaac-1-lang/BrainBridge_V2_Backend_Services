package com.learn.brainbridge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite key class for ProjectTag entity
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTagId implements Serializable {
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "tag_id")
    private Long tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectTagId that = (ProjectTagId) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, tagId);
    }
}
