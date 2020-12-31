package com.magic.interview.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

/**
 * Created by yang on 2017/5/24.
 */
@Slf4j
public class RunShellUtils {

    private static final String cmd_ffmpeg = "/usr/local/ffmpeg/bin/ffmpeg";
    //    private static final String cmd_ffmpeg = "D:\\application\\program\\ffmpeg-3.3.2-win64\\bin\\ffmpeg.exe";
    private static final String cmd_ffprobe = "/usr/local/ffmpeg/bin/ffprobe";

    private static final String cmd_convert = "/usr/bin/convert ";


    public static String getVideoSize(String filepath) {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "1000x1000";
        }

        System.out.println("videoSize_filepath:" + filepath);
        //export LD_LIBRARY_PATH=/usr/local/ffmpeg/lib/:/usr/local/lib;
        String cmd = cmd_ffprobe + " -i " + filepath + " -show_streams 2>&1 | grep \"width\\|height\\|rotation\" | grep -v coded | awk -F\"=\" '{a[$1]=$2}END{if(length(a[\"rotation\"])>0){print a[\"height\"]\"x\"a[\"width\"]} else {print a[\"width\"]\"x\"a[\"height\"]}}'";
//        String cmd = "/usr/local/ffmpeg/bin/ffprobe -i " + filepath + " -show_streams 2>&1 | grep \"width\\|height\" | grep -v coded | awk -F\"=\" '{a[$1]=$2}END{print a[\"width\"]\"x\"a[\"height\"]}'";
        System.out.println("videoSize_filepath:" + filepath);
        return getRunShellResult(cmd);
    }

    public static String getVideoCut(String filepath, String newFile, String start, String duration) {
        System.out.println("filepath:" + filepath);
        String cmd = cmd_ffmpeg + " -i  " + filepath + " -vcodec copy -acodec copy -ss " + start + " -t " + duration + " " + newFile + " -y -v quiet";
        System.out.println("newFile:" + newFile);
        return getRunShellResult(cmd);
    }

    //去除噪音
    public static void removieSound(String filepath, String newFile) {
        System.out.println("filepath:" + filepath);
        String cmd = cmd_ffmpeg + " -i  " + filepath + " -af silenceremove=1:0:-50dB:-1:0:-50dB " + newFile;
        System.out.println("newFile:" + newFile);
        getRunShellResult(cmd);
    }

    //获取音频总播放时长
    public static String getAudioDuration(String filepath) {
        String cmd = cmd_ffprobe + " -i " + filepath + " -show_format 2>&1 | grep Duration|awk -F , '{print $1}' |awk -F ' ' '{print $2}' ";
        System.out.println("file:" + filepath + "|cmd:" + cmd);
        return getRunShellResult(cmd);
    }

    //获取音频采样率
    public static String getAuditSampleRate(String filepath) {
        String cmd = cmd_ffprobe + " -i " + filepath + " -show_format 2>&1 | grep Hz|awk -F , '{print $2}' |awk -F ' ' '{print $1}' ";
        System.out.println("file:" + filepath + "|cmd:" + cmd);
        return getRunShellResult(cmd);
    }

    public static String getFileSize(String filepath) {
        System.out.println("fileSize_filepath:" + filepath);
        String cmd = "/usr/bin/ls -l " + filepath + " | awk '{print $5}'";
        System.out.println("fileSize_filepath:" + filepath);
        return getRunShellResult(cmd);
    }


    public static String getCover(String videoFilepath, String outFilePath) {
        return getCover(videoFilepath, outFilePath, "5.000");
    }

    public static String getCover(String videoFilepath, String outFilePath, String time) {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "http://small-bronze.oss-cn-shanghai.aliyuncs.com/image/video/cover/2017/9/13/2F4428D5955D4318B779894DB4290E10.jpg";
        }

        ///usr/local/ffmpeg/bin/ffmpeg -i output.mp4 -y -loglevel quiet -f image2 -ss 37.050 -t 0.001 -s 352x240 b.jpg
        System.out.println("videoFilepath:" + videoFilepath + "|outfilepath=" + outFilePath);
        String cmd = cmd_ffmpeg + " -i " + videoFilepath + " -y -loglevel quiet -f image2 -ss " + time + " " + outFilePath;
        System.out.println("outfilepath:" + outFilePath);
        return getRunShellResult(cmd);
    }

    public static String getDuration(String filepath) {

        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "1000";
        }
        System.out.println("fileSize_duration:" + filepath);
        //export LD_LIBRARY_PATH=/usr/local/ffmpeg/lib/:/usr/local/lib;
        String cmd = cmd_ffprobe + " -i " + filepath + " -show_format 2>&1 | grep duration | sed -n 's/duration=//p'";
        System.out.println("fileSize_duration:" + cmd);
        String old_duration = getRunShellResult(cmd);
        if (!StringUtils.isEmpty(old_duration)) {
//            if (old_duration.contains(".")){
//                return old_duration.substring(0,old_duration.indexOf("."));
//            }else{
            return old_duration;
//            }
        } else {
            return "0";
        }
    }

    public static int getWget(String sourcepath, String localPath) {
        return getRunShell("/usr/bin/wget -c  " + sourcepath + " -q -t 3 -O " + localPath);
    }
    //视频打水印，视频前半时间logo显示在左上角，后半时间段logo显示在右上角
    //执行例子：ffmpeg -i qq.mp4 -i /root/640x360_2.png -i /root/640x360_2.png -filter_complex "overlay=x='if(lte(t\,5)\,0\,NAN)'[out];[out][2]overlay=x='if(gte(t\,5)\,main_w-overlay_w-0\,NAN)'" b640.mp4 -y -v quiet
    // 参数 ffmpeg 命令 -i qq.mp4 源视频 -i 第一个logo -i 第二个logo filter_complex滤镜 overlay=x='if(lte(t\,5)\,0\,NAN)'[out];' 就是左上角显示的logo判断条件，就是如果时间小于5秒时指定x坐标为0,并输出内容[out];
    // [out][2] 是指定左上角的logo处理完后作为下一个流的参数,[2] 指第二个logo、overlay=x='if(gte(t\,5)\,main_w-overlay_w-0\,NAN)' 就是右上角显示的logo判断条件，就是如果时间大于5秒时指定x坐标为0;
    //b640.mp4 最终输出视频文件
    public static void watermark(String videofile,String onelogo,String twologo,String outvideofile,int d){
        if (!new File(videofile).exists()) {
            throw new RuntimeException("video file does't exist !!");
        }
        if (StringUtils.isEmpty(onelogo)) {
            throw new RuntimeException("onelogo file path is empty !!");
        }
        if (StringUtils.isEmpty(twologo)) {
            throw new RuntimeException("twologo file path is empty !!");
        }
        if (StringUtils.isEmpty(outvideofile)) {
            throw new RuntimeException("out file path is empty !!");
        }
        StringBuffer cmdBuffer = new StringBuffer(cmd_ffmpeg).append(" -i ").append(videofile).append(" -i ").append(onelogo)
                .append(" -i ").append(twologo).append(" -filter_complex \"")
                .append(" overlay=x='if(lte(t\\,").append(d).append(")\\,0\\,NAN)'[out];[out][2]overlay=x='if(gte(t\\,").append(d).append(")\\,main_w-overlay_w-0\\,NAN)'\"")
                .append(" ").append(outvideofile).append(" -y -v quiet");
        System.out.println("watermark="+cmdBuffer.toString());
        getRunShellResult(cmdBuffer.toString());
    }
    //图片缩放
    public static void zoomLogo(String inputLogo,String outLogo,String zoom){
        System.out.println("inputLogo=================================="+inputLogo);
        if (!new File(inputLogo).exists()){
            throw new RuntimeException("zoomLogo inputLogo file does't exist !!");
        }
        StringBuffer cmdBuffer = new StringBuffer(cmd_convert).append(" -quality 100 -resize ").append(zoom).append(" ").append(inputLogo)
                .append(" ").append(outLogo);
        System.out.println("zoomLogo="+cmdBuffer.toString());
        getRunShellResult(cmdBuffer.toString());
    }
    public static void deLogo(String videoFilePath, String outFilePath, List<String[]> logoInfoList) {

        if (!new File(videoFilePath).exists()) {
            throw new RuntimeException("video file does't exist !!");
        }
        if (StringUtils.isEmpty(outFilePath)) {
            throw new RuntimeException("out file path is empty !!");
        }
        if (CollectionUtils.isEmpty(logoInfoList)) {
            throw new RuntimeException("logo info list is empty !!");
        }
        for (String[] logoInfo : logoInfoList) {
            if (null == logoInfo || logoInfo.length != 4) {
                throw new RuntimeException("logo info is empty or it's length isn't 4 ");
            }
        }
//        -i A.mp4 -filter_complex "delogo=x=20:y=10:w=100:h=120[out0];[out0]delogo=x=550:y=10:w=100:h=120[out1]" -map "[out1]" -map 0:a -y delogo.mp4
        StringBuffer cmdBuffer = new StringBuffer(cmd_ffmpeg).append(" -i ").append(videoFilePath).append(" -filter_complex \"");
        final int[] index = {-1};
        logoInfoList.parallelStream().forEachOrdered(logoInfo -> {
            index[0]++;
            cmdBuffer
                    .append("delogo=")
                    .append("x=").append(logoInfo[0]).append(":")
                    .append("y=").append(logoInfo[1]).append(":")
                    .append("w=").append(logoInfo[2]).append(":")
                    .append("h=").append(logoInfo[3])
                    .append("[out").append(index[0]).append("]");
            if (index[0] != logoInfoList.size() - 1) {
                cmdBuffer.append(";[out").append(index[0]).append("]");
            } else {
                cmdBuffer.append("\" ");
            }
        });
        cmdBuffer.append("-map ").append("\"[out").append(index[0]).append("]\" ").append("-map 0:a -y ").append(outFilePath);
        System.out.println(cmdBuffer.toString());
        getRunShellResult(cmdBuffer.toString());
    }


    /**
     * @param filePath 视频路径
     * @param interval 每个切片的时间间隔
     * @param m3u8Name m3u8 文件名
     * @return
     */
    public static String sectForm3u8(String filePath, int interval, String m3u8Name) throws Exception {

        //视频父级路径
        String fileDir = filePath.substring(0, filePath.lastIndexOf(File.separator) + 1);
        System.out.println("========================= section for m3u8 start =========================");
        String m3u8Dir = fileDir + m3u8Name + File.separator;
        File m3u8DirFile = new File(m3u8Dir);
        if (!m3u8DirFile.exists()) {
            if (!m3u8DirFile.mkdirs()) {
                throw new RuntimeException("视频路径不存在");
            }
        }
        String cmd = cmd_ffmpeg + " -i " + filePath + " -hls_time " + interval + " -hls_list_size 50 " + m3u8Dir + m3u8Name + ".m3u8";
        getRunShellResult(cmd);
//        runCmdInWindows(cmd);
        System.out.println("========================= section for m3u8 end =========================");
        return m3u8Dir;
    }

    private static String getRunShellResult(String cmd) {
        log.info("cmd:" + cmd);
        Process ps;
        StringBuilder result = new StringBuilder();
        try {

            ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd}, null, null);
            BufferedInputStream in = new BufferedInputStream(ps.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                result.append(lineStr);
            }
            log.info("result:" + result);
            // 关闭输入流
            br.close();
            in.close();
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static int getRunShell(String cmd) {
        log.info("cmd:" + cmd);
        Process ps;
        try {

            ps = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", cmd});
            ps.waitFor();
            return ps.exitValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * windows 下需要修改 cmd_ffmpeg的路径 !!!!!!!
     *
     * @param cmd
     */
    private static void runCmdInWindows(String cmd) {

        String result = StringUtils.EMPTY;
        try {
            Process process = Runtime.getRuntime().exec(cmd + " -y -v quiet");
            BufferedInputStream in = new BufferedInputStream(process.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String lineStr;
            while ((lineStr = br.readLine()) != null) {
                result = lineStr;
            }
            System.out.println("result:" + result);
            // 关闭输入流
            br.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回视频的相关信息
     *
     * @param localPath
     * @return
     */
    public static String getVideoInfo(String localPath) {
        String cmd = cmd_ffprobe + " -v 0 -of json -show_streams " + localPath;
        String videoInfoStr = getRunShellResult(cmd);
        try {
            return videoInfoStr;
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }
    
	public static boolean runShell(List<String> commands, String from) {
		try {
			ProcessBuilder builder = new ProcessBuilder();
			//log.info("from={} commands={}", from, JacksonUtil.obj2json(commands));
			//System.out.println(JacksonUtil.obj2json(commands));
			builder.command(commands);
			Process p = builder.start();
			doWaitFor(p, from);
			p.destroy();
			return true;
		} catch (Exception e) {
			log.info(from + " " + e.getMessage(), e);
			return false;
		}
	}
    
	private static int doWaitFor(Process p, String from) {
		InputStream in = null;
		InputStream err = null;
		int exitValue = -1; // returned to caller when p is finished
		try {
			log.info("from= {} coming", from);
			in = p.getInputStream();
			err = p.getErrorStream();
			boolean finished = false; // Set to true when p is finished

			while (!finished) {
				try {
					while (in.available() > 0) {
						Character c = new Character((char) in.read());
						System.out.print(c);
					}
					while (err.available() > 0) {
						Character c = new Character((char) err.read());
						System.out.print(c);
					}

					exitValue = p.exitValue();
					finished = true;

				} catch (IllegalThreadStateException e) {
					Thread.sleep(500);
				}
			}
		} catch (Exception e) {
			log.info("from={} doWaitFor(); unexpected exception - {}", from, e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}

			} catch (IOException e) {
				log.info("from={} doWaitFor(); in IOException - {}", from, e.getMessage());
			}
			if (err != null) {
				try {
					err.close();
				} catch (IOException e) {
					log.info("from={} doWaitFor(); err IOException - {}", from, e.getMessage());
				}
			}
		}
		return exitValue;
	}
}
