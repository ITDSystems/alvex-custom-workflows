[![Build Status](https://travis-ci.org/ITDSystems/alvex-custom-workflows.svg?branch=master)](https://travis-ci.org/ITDSystems/alvex-custom-workflows)

Alvex custom workflows component
========================

Includes a number of workflow for Alfresco that presents features of Alvex components and can be used for generic use cases:
* *Assign a task* workflow allows users to set a task or to give the instructions to a colleague. The result of the task can be reviewed by the workflow initiator.
* *Request Document Approval* workflow allows to submit a document for the review. In the case of rejection of the document it can be reworked and submitted for the review again.
* *Distribute Documents* workflow send documents to selected users without any control.

Compatible with Alfresco 5.1.

This component depends on:
* [Alvex Utils](https://github.com/ITDSystems/alvex-utils)
* [Alvex Orgchart](https://github.com/ITDSystems/alvex-orgchart)
* [Alvex Uploader](https://github.com/ITDSystems/alvex-uploader)
* [Alvex Workflow Permissions](https://github.com/ITDSystems/alvex-workflow-permissions)

# Downloads

Download ready-to-use Alvex components via [Alvex](https://github.com/ITDSystems/alvex#downloads).

# Build from source

To build Alvex follow [this guide](https://github.com/ITDSystems/alvex#build-component-from-source).

# Use

### Orgchart basics and configuration in UI

### Assign a Task

Workflow described in Alvex documentation: [http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_assign_a_task](http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_assign_a_task)

### Request Document Approval

Workflow described in Alvex documentation: [http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_request_document_approval](http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_request_document_approval)

### Distribute Documents

Workflow described in Alvex documentation: [http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_distribute_documents](http://docs.alvexcore.com/en-US/Alvex/2.0.3/html-single/User_Guide/#workflows_distribute_documents)

**Note:** After installing this component you need to add new workflows to a list of allowed workflows on Workflow Permissions page in Admin console. If you don't do it, users will not be able to start these workflows.


