package com.example.oirapp.ui.state

sealed class GroupState {
    data object Create : GroupState()
    data object Edit : GroupState()
}
