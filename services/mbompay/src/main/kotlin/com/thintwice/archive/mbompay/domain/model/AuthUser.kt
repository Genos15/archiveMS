package com.thintwice.archive.mbompay.domain.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthUser(
    private val _username: String,
    private val _password: String,
    private val roles: MutableCollection<out GrantedAuthority> = mutableListOf(),
    private val _isAccountNonExpired: Boolean = false,
    private val _isAccountNonLocked: Boolean = false,
    private val _isCredentialsNonExpired: Boolean = false,
    private val _isEnabled: Boolean = false,
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = roles

    override fun getPassword(): String = _password

    override fun getUsername(): String = _username

    override fun isAccountNonExpired(): Boolean = _isAccountNonExpired

    override fun isAccountNonLocked(): Boolean = _isAccountNonLocked

    override fun isCredentialsNonExpired(): Boolean = _isCredentialsNonExpired

    override fun isEnabled(): Boolean = _isEnabled
}