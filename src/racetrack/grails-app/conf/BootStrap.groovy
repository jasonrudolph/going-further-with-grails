class BootStrap {

    def init = { servletContext ->

        // populate initial authorization data if it is the DB is empty
        def personList = Person.list()
        if(personList.size()==0) {
          log.info('Populating default authorization data')
          def pass = org.apache.commons.codec.digest.DigestUtils.md5Hex("pass")
      
          // create admin user
          def admin = new Person(
            username:"admin", userRealName:"Admin Joe", passwd:pass,
            enabled:true, email:"admin@example.com",
            description:"Admin User"
          )
          admin.save()

          // a group staff
          def staffDude = new Person(
            username:"staff", userRealName:"Staff Stan", passwd:pass,
            enabled:true, email:"stan@example.com", description:"Group staff"
          )
          staffDude.save()

          // create ADMIN Authority
          def adminAuth =
            new Authority(authority:"ROLE_ADMIN", description:"Group ADMINS")
          adminAuth.addToPeople(admin)
          adminAuth.save()

    	// create GROUP_USER Authority
          def roleGroupStaffAuth =
            new Authority(authority:"ROLE_GROUP_USER", description:"Group Staff")
          roleGroupStaffAuth.addToPeople(staffDude)
          roleGroupStaffAuth.save()
      
          new Requestmap(url:"/**", configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
          new Requestmap(url:"/login/**", configAttribute:"IS_AUTHENTICATED_ANONYMOUSLY").save()
          new Requestmap(url:"/person/**", configAttribute:"ROLE_ADMIN").save()
          new Requestmap(url:"/authority/**", configAttribute:"ROLE_ADMIN").save()
          new Requestmap(url:"/requestmap/**", configAttribute:"ROLE_ADMIN").save()
        }
     }
        
     def destroy = {
     }

} 