package info.isaaclee.lolgoitne.core.application.service.riot

import info.isaaclee.lolgoitne.core.application.port.out.http.FindMatchOutPort
import info.isaaclee.lolgoitne.core.application.port.out.http.FindMatchesOutPort
import info.isaaclee.lolgoitne.core.application.service.riot.exceptions.MatchNotFoundException
import info.isaaclee.lolgoitne.core.domain.riot.Match
import java.text.SimpleDateFormat
import javax.inject.Named

@Named
class FindLastMatchService(
  private val findMatchesOutPort: FindMatchesOutPort,
  private val findMatchOutPort: FindMatchOutPort
) {
  fun lastPlayingDate(nickname: String, puuid: String): String {
    val match: Match
    try {
      val queryParams: MutableMap<String, String> = mutableMapOf()
      queryParams["count"] = "1"
  
      val matchIds = findMatchesOutPort.findMatchIds(puuid, queryParams)
      match = findMatchOutPort.findMatchById(matchIds[0])
    } catch (ex: MatchNotFoundException) {
      return "${nickname}은 게임 전적이 없어요!"
    } catch (ex: Exception) {
      return INTERNAL_SERVER_ERROR_MESSAGE
    }
    val lastDate = SimpleDateFormat("yyyy년 MM월 dd일 hh시 mm분").format(match.info.gameEndTimestamp)
    return "앗! ${nickname}님은 지금 게임 중이 아니네요. ${nickname}님이 마지막으로 게임을 한 시간은 ${lastDate}이에요."
  }
}