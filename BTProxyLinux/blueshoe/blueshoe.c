// Special thanks to:
// Sample code taken from:
//   ipheth driver located in sample_resources/ipheth/ipheth.c

// Comprehensive network driver info:
//   http://www.makelinux.info/ldd3/chp-17.shtml
//   http://www.techveda.in/index.php/network-driver-tutorial
// General driver info:
//   http://www.freesoftwaremagazine.com/articles/drivers_linux
//   http://black-coders.net/articles/linux/linux-kernel-modules.php

// If your system utterly breaks, you can still perform a safe reboot:
//   http://en.wikipedia.org/wiki/Magic_SysRq_key

#include <linux/module.h> // standard for all kernel modules
#include <linux/netdevice.h> // includes net_device
#include <linux/etherdevice.h> // includes alloc_etherdev

MODULE_AUTHOR ("Samuel Pauls");
MODULE_LICENSE ("GPL");
MODULE_DESCRIPTION ("Desktop driver for a bluetooth tethering application.");

struct ipheth_device {
  struct net_device *dev;
  // private data for custom data that's used by the driver goes here
};

struct net_device *gdev = NULL; // needed by shoe_exit()

static int ipheth_open (struct net_device *net)
{
	//struct ipheth_device *dev = netdev_priv(net); // recommended to get pointer to private data through netdev_priv, instead of accessing it directly
	return 0;
}

static int ipheth_close (struct net_device *net)
{
	//struct ipheth_device *dev = netdev_priv(net);
	return 0;
}

static int ipheth_tx (struct sk_buff *skb, struct net_device *net)
{
	//struct ipheth_device *dev = netdev_priv(net);
	return NETDEV_TX_OK;
}

static void ipheth_tx_timeout (struct net_device *net)
{
	//struct ipheth_device *dev = netdev_priv(net);
}

static struct net_device_stats *ipheth_stats (struct net_device *net)
{
	//struct ipheth_device *dev = netdev_priv(net);
	return NULL;
}

static const struct net_device_ops ipheth_netdev_ops = {
       .ndo_open = ipheth_open,
       .ndo_stop = ipheth_close,
       .ndo_start_xmit = ipheth_tx,
       .ndo_tx_timeout = ipheth_tx_timeout,
       .ndo_get_stats = ipheth_stats,
};

static int shoe_init (void) {
  printk ("<1>Initializing blueshoe!\n");

  // Allocate and initialize common ethernet device parameters.
  struct net_device *dev = alloc_etherdev (sizeof (struct ipheth_device));
  if (dev == NULL) { // is NULL even possible here?
    printk ("<1>Failed to allocate blueshoe!\n");
  }

  // Setup the private data.
  struct ipheth_device *eth_device;
  eth_device = netdev_priv(dev);
  eth_device->dev = dev;
  gdev = dev;

  // Other required initialization.
  // TODO register_netdev screws up the OS before returning because the
  // net_device passed to it hasn't been fully initialized.  But what's left to
  // initialize?
  dev->netdev_ops = &ipheth_netdev_ops;
  memcpy (dev->dev_addr, "macaddressgoesheretesttesttest", ETH_ALEN);
  dev->watchdog_timeo = 5 * HZ;
  dev->features = 0; // maybe if we have no features, less initialization will
                     // be required to get the driver to load without messing
                     // up OS

  // Register the device with the kernel.
  printk ("<1>Attempting to register blueshoe!\n");
  static const int REGISTER_SUCCESSFUL = 0;
  if (register_netdev (dev) == REGISTER_SUCCESSFUL) {
    printk ("<1>Registered blueshoe!\n");
  } else {
    printk ("<1>Failed to register blueshoe!\n");
  }

  static const int SUCCESS = 0;
  return SUCCESS;
}

static void shoe_exit (void) {
  if (gdev != NULL) {
    unregister_netdev (gdev);
    free_netdev (gdev);
  }

  // printk is similar to printf
  // only works in the kernel
  // <1> for high priority
  printk ("<1>Blueshoe destroyed!\n");
}

module_init (shoe_init); // map to insmod
module_exit (shoe_exit); // map to rmmod
