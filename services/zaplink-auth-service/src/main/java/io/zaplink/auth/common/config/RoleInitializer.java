package io.zaplink.auth.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.zaplink.auth.entity.Role;
import io.zaplink.auth.repository.RoleRepository;

@Component
public class RoleInitializer
    implements
    CommandLineRunner
{
    private final RoleRepository roleRepository;
    public RoleInitializer( RoleRepository roleRepository )
    {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run( String... args )
        throws Exception
    {
        if ( roleRepository.count() == 0 )
        {
            initializeRoles();
        }
    }

    private void initializeRoles()
    {
        Role userRole = new Role();
        userRole.setName( "USER" );
        userRole.setDescription( "Standard user role" );
        roleRepository.save( userRole );
        Role adminRole = new Role();
        adminRole.setName( "ADMIN" );
        adminRole.setDescription( "Administrator role" );
        roleRepository.save( adminRole );
        Role moderatorRole = new Role();
        moderatorRole.setName( "MODERATOR" );
        moderatorRole.setDescription( "Moderator role" );
        roleRepository.save( moderatorRole );
    }
}
