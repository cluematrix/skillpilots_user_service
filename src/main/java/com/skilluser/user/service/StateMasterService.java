package com.skilluser.user.service;

import com.skilluser.user.dto.StateResponseDTO;
import com.skilluser.user.model.StateMaster;

import java.util.List;

public interface StateMasterService
{
    StateMaster saveState(StateMaster stateMaster);
    StateResponseDTO updateState(StateMaster stateMaster);
    List<StateResponseDTO> getAllStates();
    void deleteState(Long stateId);

}
