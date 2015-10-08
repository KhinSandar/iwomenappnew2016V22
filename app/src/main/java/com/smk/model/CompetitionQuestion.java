package com.smk.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompetitionQuestion {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("question")
@Expose
private String question;
@SerializedName("question_mm")
@Expose
private String questionMm;
@SerializedName("description")
@Expose
private String description;
@SerializedName("description_mm")
@Expose
private String descriptionMm;
@SerializedName("instruction_about_game")
@Expose
private String instructionAboutGame;
@SerializedName("start_date")
@Expose
private String startDate;
@SerializedName("end_date")
@Expose
private String endDate;
@SerializedName("day_left")
@Expose
private String dayLeft;
@SerializedName("current_datetime")
@Expose
private String currentDatetime;
@SerializedName("status")
@Expose
private String status;
@SerializedName("created_at")
@Expose
private String createdAt;
@SerializedName("updated_at")
@Expose
private String updatedAt;
@SerializedName("correct_answer")
@Expose
private CorrectAnswer correctAnswer;
@SerializedName("group_user")
@Expose
private GroupUser groupUser;
@SerializedName("next_question")
@Expose
private NextQuestion nextQuestion;

/**
* 
* @return
* The id
*/
public Integer getId() {
return id;
}

/**
* 
* @param id
* The id
*/
public void setId(Integer id) {
this.id = id;
}

/**
* 
* @return
* The question
*/
public String getQuestion() {
return question;
}

/**
* 
* @param question
* The question
*/
public void setQuestion(String question) {
this.question = question;
}

/**
* 
* @return
* The questionMm
*/
public String getQuestionMm() {
return questionMm;
}

/**
* 
* @param questionMm
* The question_mm
*/
public void setQuestionMm(String questionMm) {
this.questionMm = questionMm;
}

/**
* 
* @return
* The description
*/
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The descriptionMm
*/
public String getDescriptionMm() {
return descriptionMm;
}

/**
* 
* @param descriptionMm
* The description_mm
*/
public void setDescriptionMm(String descriptionMm) {
this.descriptionMm = descriptionMm;
}


/**
* 
* @return
* The instructionAboutGame
*/
public String getInstructionAboutGame() {
return instructionAboutGame;
}

/**
* 
* @param instructionAboutGame
* The instruction_about_game
*/
public void setInstructionAboutGame(String instructionAboutGame) {
this.instructionAboutGame = instructionAboutGame;
}

/**
* 
* @return
* The startDate
*/
public String getStartDate() {
return startDate;
}

/**
* 
* @param startDate
* The start_date
*/
public void setStartDate(String startDate) {
this.startDate = startDate;
}

/**
* 
* @return
* The endDate
*/
public String getEndDate() {
return endDate;
}

/**
* 
* @param endDate
* The end_date
*/
public void setEndDate(String endDate) {
this.endDate = endDate;
}

/**
* 
* @return
* The dayLeft
*/
public String getDayLeft() {
return dayLeft;
}

/**
* 
* @param dayLeft
* The day_left
*/
public void setDayLeft(String dayLeft) {
this.dayLeft = dayLeft;
}

/**
* 
* @return
* The currentDatetime
*/
public String getCurrentDatetime() {
return currentDatetime;
}

/**
* 
* @param currentDatetime
* The current_datetime
*/
public void setCurrentDatetime(String currentDatetime) {
this.currentDatetime = currentDatetime;
}

/**
* 
* @return
* The status
*/
public String getStatus() {
return status;
}

/**
* 
* @param status
* The status
*/
public void setStatus(String status) {
this.status = status;
}

/**
* 
* @return
* The createdAt
*/
public String getCreatedAt() {
return createdAt;
}

/**
* 
* @param createdAt
* The created_at
*/
public void setCreatedAt(String createdAt) {
this.createdAt = createdAt;
}

/**
* 
* @return
* The updatedAt
*/
public String getUpdatedAt() {
return updatedAt;
}

/**
* 
* @param updatedAt
* The updated_at
*/
public void setUpdatedAt(String updatedAt) {
this.updatedAt = updatedAt;
}

/**
* 
* @return
* The correctAnswer
*/
public CorrectAnswer getCorrectAnswer() {
return correctAnswer;
}

/**
* 
* @param correctAnswer
* The correct_answer
*/
public void setCorrectAnswer(CorrectAnswer correctAnswer) {
this.correctAnswer = correctAnswer;
}

/**
* 
* @return
* The groupUser
*/
public GroupUser getGroupUser() {
return groupUser;
}

/**
* 
* @param groupUser
* The group_user
*/
public void setGroupUser(GroupUser groupUser) {
this.groupUser = groupUser;
}

/**
* 
* @return
* The nextQuestion
*/
public NextQuestion getNextQuestion() {
return nextQuestion;
}

/**
* 
* @param nextQuestion
* The next_question
*/
public void setNextQuestion(NextQuestion nextQuestion) {
this.nextQuestion = nextQuestion;
}

}