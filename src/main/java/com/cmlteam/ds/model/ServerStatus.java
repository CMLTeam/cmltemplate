package com.cmlteam.ds.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author vgorin
 *         file created on 9/10/17 5:18 PM
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerStatus {
	public int totalAccounts;
}
