# Test It Allure Plugin

## Purpose 
Plugin for creating Allure annotations for autotests linked with TMS 'Test It'. 

**@TmsLink** uses to set TestIt autotest's **external_id**
**@Link** with type **manual** uses to link autotest with manual tests 
   
#### Plugin was build to work only with the following libraries::
    Junit 5 / Test NG
    Allure     
    
##### How to use:
    1) Set up valid credentials in plugin settings under Tools / TestIt Allure plugin
    2) Go to your autotests
    3) Put a cursor on a test's method name and press Alt+Insert for open 'Generate' context menu
    4) Select 'Add TestIt autotest annotations' option
    5) In opened popup set 'external_id' of autotest and linked manual tests ids (separated by comma)
    6) Press 'Ok' and relevant Allure's annotations will be created:
    
     - @TmsLink with randomly generated UUID or manually entered value
     _ @Link with type: manual and value = UUID of test in TestIt
         
