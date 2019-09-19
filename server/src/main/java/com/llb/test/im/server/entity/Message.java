package com.llb.test.im.server.entity;

import java.util.Date;

public class Message {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private Long id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.source_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private Long sourceUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.target_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private Long targetUserId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.request_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private String requestId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.content
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private String content;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.created_at
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private Date createdAt;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column message.status
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    private Integer status;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.id
     *
     * @return the value of message.id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.id
     *
     * @param id the value for message.id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.source_user_id
     *
     * @return the value of message.source_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public Long getSourceUserId() {
        return sourceUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.source_user_id
     *
     * @param sourceUserId the value for message.source_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setSourceUserId(Long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.target_user_id
     *
     * @return the value of message.target_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public Long getTargetUserId() {
        return targetUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.target_user_id
     *
     * @param targetUserId the value for message.target_user_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.request_id
     *
     * @return the value of message.request_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.request_id
     *
     * @param requestId the value for message.request_id
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId == null ? null : requestId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.content
     *
     * @return the value of message.content
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.content
     *
     * @param content the value for message.content
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.created_at
     *
     * @return the value of message.created_at
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.created_at
     *
     * @param createdAt the value for message.created_at
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column message.status
     *
     * @return the value of message.status
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column message.status
     *
     * @param status the value for message.status
     *
     * @mbg.generated Thu Sep 19 23:01:00 CST 2019
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}