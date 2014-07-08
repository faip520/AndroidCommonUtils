#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include <sys/stat.h>

#include <unistd.h>

/**
 * 把这个文件通过ndk-build，可以变成一个可执行的二进制程序，放到/system/xbin下面
 * chown 0:0 a1w0n后更改成为root用户所有，然后chmod 06755 a1w0n设置s位
 * 然后这个文件就可以当成su一样来用了
 *
 * 用法 a1w0n -c "ll -s" 或者 a1w0n -c "chmod 777 aa.b"
 */

int exe(int argc, char* argv[]);
int main(int argc, char* argv[]) {
	return exe(argc, argv);
}

static int executionFailure(char *context) {
	fprintf(stderr, "su: %s. Error:%s\n", context, strerror(errno));
	return -errno;
}

static int permissionDenied() {
	printf("su: permission denied\n");
	return 1;
}

int exe(int argc, char* argv[]) {
	// ===给Shell进程赋予root权限===
	int uid = 0;
	int gid = 0;

	if (setgid(gid) || setuid(uid))
		return permissionDenied();
	// ======================
	
	// 设置环境变量
	char* const envp = "PATH=/vendor/bin:/system/bin:/system/xbin";
	putenv(envp);
	char* const envp2 = "LD_LIBRARY_PATH=/vendor/lib:/system/lib";
	putenv(envp2);

	char *exec_args[argc + 1];
	// execv的第二个参数必须NULL结尾
	exec_args[argc] = NULL;
	exec_args[0] = "sh";
	
	int i;
	for (i = 1; i < argc; i++)
	{
		exec_args[i] = argv[i];
		
		// 调试用
//		char Com[80];
//		strcpy(Com,"main cmd: ");
//		strcat(Com,exec_args[i]);
//		puts(Com);
	}
	
	execv("/system/bin/sh", exec_args);
	// 如果执行成功，这句return语句就永远没机会执行了
	// 只有execv执行失败的情况下 这句return语句才有机会执行
	return executionFailure("sh");
}
