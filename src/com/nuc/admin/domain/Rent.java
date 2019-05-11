package com.nuc.admin.domain;

import com.soft.common.domain.BaseDomain;

public class Rent extends BaseDomain {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.rent_id
     *
     * @mbg.generated
     */
    private Integer rentId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.rent_price
     *
     * @mbg.generated
     */
    private Double rentPrice;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.rent_start
     *
     * @mbg.generated
     */
    private String rentStart;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.rent_end
     *
     * @mbg.generated
     */
    private String rentEnd;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.rent_money
     *
     * @mbg.generated
     */
    private Double rentMoney;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column rent.room_id
     *
     * @mbg.generated
     */
    private Integer roomId;


    private String room_no; //
    private int user_id; //
    private String real_name; //
    private String ids;
    private String random;

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.rent_id
     *
     * @return the value of rent.rent_id
     *
     * @mbg.generated
     */
    public Integer getRentId() {
        return rentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.rent_id
     *
     * @param rentId the value for rent.rent_id
     *
     * @mbg.generated
     */
    public void setRentId(Integer rentId) {
        this.rentId = rentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.rent_price
     *
     * @return the value of rent.rent_price
     *
     * @mbg.generated
     */
    public Double getRentPrice() {
        return rentPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.rent_price
     *
     * @param rentPrice the value for rent.rent_price
     *
     * @mbg.generated
     */
    public void setRentPrice(Double rentPrice) {
        this.rentPrice = rentPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.rent_start
     *
     * @return the value of rent.rent_start
     *
     * @mbg.generated
     */
    public String getRentStart() {
        return rentStart;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.rent_start
     *
     * @param rentStart the value for rent.rent_start
     *
     * @mbg.generated
     */
    public void setRentStart(String rentStart) {
        this.rentStart = rentStart == null ? null : rentStart.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.rent_end
     *
     * @return the value of rent.rent_end
     *
     * @mbg.generated
     */
    public String getRentEnd() {
        return rentEnd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.rent_end
     *
     * @param rentEnd the value for rent.rent_end
     *
     * @mbg.generated
     */
    public void setRentEnd(String rentEnd) {
        this.rentEnd = rentEnd == null ? null : rentEnd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.rent_money
     *
     * @return the value of rent.rent_money
     *
     * @mbg.generated
     */
    public Double getRentMoney() {
        return rentMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.rent_money
     *
     * @param rentMoney the value for rent.rent_money
     *
     * @mbg.generated
     */
    public void setRentMoney(Double rentMoney) {
        this.rentMoney = rentMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column rent.room_id
     *
     * @return the value of rent.room_id
     *
     * @mbg.generated
     */
    public Integer getRoomId() {
        return roomId;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId=" + rentId +
                ", rentPrice=" + rentPrice +
                ", rentStart='" + rentStart + '\'' +
                ", rentEnd='" + rentEnd + '\'' +
                ", rentMoney=" + rentMoney +
                ", roomId=" + roomId +
                ", room_no='" + room_no + '\'' +
                ", user_id=" + user_id +
                ", real_name='" + real_name + '\'' +
                ", ids='" + ids + '\'' +
                ", random='" + random + '\'' +
                '}';
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column rent.room_id
     *
     * @param roomId the value for rent.room_id
     *
     * @mbg.generated
     */
    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }


}