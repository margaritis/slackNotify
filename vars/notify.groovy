#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
 */
def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  // Default values
  // def gitCommitMessage=sh(returnStdout: true, script: 'git log -1 --format=%B ${env.GIT_COMMIT}').trim()
  // def gitCommitShortSHA=sh(returnStdout: true, script: 'git rev-parse --short HEAD')
  // GIT_COMMIT_MESSAGE=sh(returnStdout: true, script: 'git log -1 --format=%B ${env.GIT_COMMIT}').trim()
  // GIT_COMMIT_SHORT=sh(returnStdout: true, script: 'git rev-parse --short HEAD')
  def decodedJobName = env.JOB_NAME.replaceAll("%2F", "/")
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: build in '${decodedJobName}' [<${env.BUILD_URL}|${env.BUILD_NUMBER}>]"
  // def summary = "${subject} \n Stage: '${env.STAGE_NAME}' (<${env.BUILD_URL}|Open>)"
  def summary = "${subject}"
  def details = """<p>${buildStatus}: Job '${decodedJobName} [${env.BUILD_NUMBER}]':</p>
    <p>STAGE: env.STAGE_NAME</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${decodedJobName} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESS') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Send notifications
  slackSend (color: colorCode, message: summary)

}
