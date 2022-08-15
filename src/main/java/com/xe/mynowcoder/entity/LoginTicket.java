package com.xe.mynowcoder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTicket {

    private int loginTicketId;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

}
