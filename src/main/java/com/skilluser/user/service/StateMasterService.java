package com.skilluser.user.service;

import com.skilluser.user.dto.StateResponseDTO;
import com.skilluser.user.model.StateMaster;

import java.util.List;

public interface StateMasterService
{
    public StateMaster saveState(StateMaster stateMaster);
    public StateResponseDTO updateState(StateMaster stateMaster);
    public List<StateResponseDTO> getAllStates();
    public void deleteState(Long stateId);

}
