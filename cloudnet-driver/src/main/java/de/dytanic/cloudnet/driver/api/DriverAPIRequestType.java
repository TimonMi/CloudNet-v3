package de.dytanic.cloudnet.driver.api;

public enum DriverAPIRequestType {

    // ServiceTaskProvider
    RELOAD_TASKS(DriverAPICategory.SERVICE_TASKS),
    SET_PERMANENT_SERVICE_TASKS(DriverAPICategory.SERVICE_TASKS),
    GET_PERMANENT_SERVICE_TASKS(DriverAPICategory.SERVICE_TASKS),
    GET_PERMANENT_SERVICE_TASK_BY_NAME(DriverAPICategory.SERVICE_TASKS),
    IS_SERVICE_TASK_PRESENT(DriverAPICategory.SERVICE_TASKS),
    ADD_PERMANENT_SERVICE_TASK(DriverAPICategory.SERVICE_TASKS),
    REMOVE_PERMANENT_SERVICE_TASK(DriverAPICategory.SERVICE_TASKS),

    // GroupConfigurationProvider
    RELOAD_GROUPS(DriverAPICategory.GROUP_CONFIGURATIONS),
    SET_GROUP_CONFIGURATIONS(DriverAPICategory.GROUP_CONFIGURATIONS),
    GET_GROUP_CONFIGURATIONS(DriverAPICategory.GROUP_CONFIGURATIONS),
    GET_GROUP_CONFIGURATION_BY_NAME(DriverAPICategory.GROUP_CONFIGURATIONS),
    IS_GROUP_CONFIGURATION_PRESENT(DriverAPICategory.GROUP_CONFIGURATIONS),
    ADD_GROUP_CONFIGURATION(DriverAPICategory.GROUP_CONFIGURATIONS),
    REMOVE_GROUP_CONFIGURATION(DriverAPICategory.GROUP_CONFIGURATIONS),

    // NodeInfoProvider
    GET_NODES(DriverAPICategory.NODE_INFO),
    GET_NODE_BY_UNIQUE_ID(DriverAPICategory.NODE_INFO),
    GET_NODE_INFO_SNAPSHOTS(DriverAPICategory.NODE_INFO),
    GET_NODE_INFO_SNAPSHOT_BY_UNIQUE_ID(DriverAPICategory.NODE_INFO),
    SEND_COMMAND_LINE(DriverAPICategory.NODE_INFO),
    SEND_COMMAND_LINE_TO_NODE(DriverAPICategory.NODE_INFO),
    TAB_COMPLETE_CONSOLE_COMMAND(DriverAPICategory.NODE_INFO),
    GET_CONSOLE_COMMANDS(DriverAPICategory.NODE_INFO),
    GET_CONSOLE_COMMAND_BY_LINE(DriverAPICategory.NODE_INFO),
    SEND_COMMAND_LINE_AS_PERMISSION_USER(DriverAPICategory.NODE_INFO),

    // TemplateStorages
    GET_TEMPLATE_STORAGES(DriverAPICategory.TEMPLATE_STORAGE),
    DEPLOY_TEMPLATE_BYTE_ARRAY(DriverAPICategory.TEMPLATE_STORAGE),
    DEPLOY_TEMPLATE_STREAM(DriverAPICategory.TEMPLATE_STORAGE),
    LOAD_TEMPLATE_ARRAY(DriverAPICategory.TEMPLATE_STORAGE),
    LOAD_TEMPLATE_STREAM(DriverAPICategory.TEMPLATE_STORAGE),
    DELETE_TEMPLATE(DriverAPICategory.TEMPLATE_STORAGE),
    CREATE_TEMPLATE(DriverAPICategory.TEMPLATE_STORAGE),
    CONTAINS_TEMPLATE(DriverAPICategory.TEMPLATE_STORAGE),
    APPEND_FILE_CONTENT(DriverAPICategory.TEMPLATE_STORAGE),
    SET_FILE_CONTENT(DriverAPICategory.TEMPLATE_STORAGE),
    CREATE_FILE(DriverAPICategory.TEMPLATE_STORAGE),
    CREATE_DIRECTORY(DriverAPICategory.TEMPLATE_STORAGE),
    CONTAINS_FILE(DriverAPICategory.TEMPLATE_STORAGE),
    DELETE_FILE(DriverAPICategory.TEMPLATE_STORAGE),
    LIST_FILES(DriverAPICategory.TEMPLATE_STORAGE),
    GET_FILE_INFO(DriverAPICategory.TEMPLATE_STORAGE),
    GET_TEMPLATES(DriverAPICategory.TEMPLATE_STORAGE),
    SHOULD_SYNC_IN_CLUSTER(DriverAPICategory.TEMPLATE_STORAGE),
    CLOSE_STORAGE(DriverAPICategory.TEMPLATE_STORAGE),

    // CloudServiceFactory
    CREATE_CLOUD_SERVICE_BY_CONFIGURATION(DriverAPICategory.CLOUD_SERVICE_FACTORY),

    // GeneralCloudServiceProvider
    GET_SERVICES_AS_UNIQUE_ID(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_SERVICES_COUNT(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_SERVICES_COUNT_BY_GROUP(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_SERVICES_COUNT_BY_TASK(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_STARTED_CLOUD_SERVICES(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICES(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICES_BY_SERVICE_TASK(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICES_BY_GROUP(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICE_BY_UNIQUE_ID(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICES_BY_ENVIRONMENT(DriverAPICategory.GENERAL_CLOUD_SERVICES),
    GET_CLOUD_SERVICE_BY_NAME(DriverAPICategory.GENERAL_CLOUD_SERVICES),

    // SpecificCloudServiceProvider
    RESTART_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    KILL_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    RUN_COMMAND_ON_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    GET_CACHED_LOG_MESSAGES_FROM_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    DEPLOY_RESOURCES_ON_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    FORCE_UPDATE_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    ADD_SERVICE_TEMPLATE_TO_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    ADD_SERVICE_REMOTE_INCLUSION_TO_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    ADD_SERVICE_DEPLOYMENT_TO_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    INCLUDE_WAITING_TEMPLATES_ON_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    INCLUDE_WAITING_INCLUSIONS_ON_CLOUD_SERVICE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    IS_CLOUD_SERVICE_VALID(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),
    SET_CLOUD_SERVICE_LIFE_CYCLE(DriverAPICategory.SPECIFIC_CLOUD_SERVICE),

    // PermissionManagement
    PERMISSION_MANAGEMENT_RELOAD(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_ADD_USER(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_UPDATE_USER(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_DELETE_USERS_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_DELETE_USER(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_SET_USERS(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_ADD_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_UPDATE_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_DELETE_GROUP_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_DELETE_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_SET_GROUPS(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_CONTAINS_USER_BY_UNIQUE_ID(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_CONTAINS_USER_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_USER_BY_UNIQUE_ID(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_USERS_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_FIRST_USER_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_USERS(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_USERS_BY_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_CONTAINS_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_GROUP_BY_NAME(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_GROUPS(DriverAPICategory.PERMISSION_MANAGEMENT),
    PERMISSION_MANAGEMENT_GET_DEFAULT_GROUP(DriverAPICategory.PERMISSION_MANAGEMENT);

    private final DriverAPICategory category;

    DriverAPIRequestType(DriverAPICategory category) {
        this.category = category;
    }

    public DriverAPICategory getCategory() {
        return this.category;
    }
}
